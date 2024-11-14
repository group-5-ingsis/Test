package com.ingsis.test.redis.consumer

import com.ingsis.test.asset.AssetService
import com.ingsis.test.languages.LanguageProvider
import com.ingsis.test.redis.producer.TestResult
import com.ingsis.test.redis.producer.TestResultProducer
import com.ingsis.test.tests.TestRepository
import com.ingsis.test.utils.JsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class TestRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.test}") streamKey: String,
  @Value("\${groups.test}") groupId: String,
  private val assetService: AssetService,
  private val testRepository: TestRepository,
  private val testResultProducer: TestResultProducer,
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    CoroutineScope(Dispatchers.IO).launch {
      processMessage(record)
    }
  }

  private suspend fun processMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value
    val testRequest = JsonUtils.deserializeTestRequest(streamValue)
    val snippetContent = assetService.getAssetContent(testRequest.author, testRequest.snippetId)
    val test = withContext(Dispatchers.IO) {
      testRepository.findById(testRequest.id)
    }.get()
    val inputs = test.userInputs
    val outputs = test.userOutputs
    val language = LanguageProvider.getLanguages()[test.language]
    if (language != null) {
      inputs.forEach { input ->
        val executionResult = language.execute(snippetContent, test.version, input)
        if (executionResult != outputs[inputs.indexOf(input)]) {
          test.testPassed = false
          testResultProducer.publishEvent(TestResult(test.id, false))
        }
      }
      test.testPassed = true
      withContext(Dispatchers.IO) {
        testRepository.save(test)
      }
      testResultProducer.publishEvent(TestResult(test.id, true))
    } else {
      throw Exception("Language not found")
    }
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }
}
