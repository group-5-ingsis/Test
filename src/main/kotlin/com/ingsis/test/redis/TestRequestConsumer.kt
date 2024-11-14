package com.ingsis.test.redis

import com.ingsis.test.asset.AssetService
import com.ingsis.test.languages.LanguageProvider
import com.ingsis.test.utils.JsonUtils
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
  private val assetService: AssetService
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value
    val test = JsonUtils.deserializeTestRequest(streamValue)
    val snippetContent = assetService.getAssetContent(test.author, test.snippetId)
    val inputs = test.userInputs
    val outputs = test.userOutputs
    val language = LanguageProvider.getLanguages()[test.language]
    if (language != null) {
      // TODO: How to integrate the user inputs?
      inputs.forEach { input ->
        val executionResult = language.execute(snippetContent, test.version, input)
        if (executionResult == outputs[inputs.indexOf(input)]) {
          test.testPassed = true
        }
      }
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
