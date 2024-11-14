package com.ingsis.test.redis.producer

import com.ingsis.test.utils.JsonUtils
import kotlinx.coroutines.reactive.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class TestResultProducer @Autowired constructor(
  @Value("\${stream.test-result}") streamKey: String,
  redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

  suspend fun publishEvent(testResult: TestResult) {
    val snippetAsJson = JsonUtils.serializeTestResult(testResult)
    emit(snippetAsJson).awaitSingle()
  }
}
