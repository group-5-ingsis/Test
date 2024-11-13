package com.ingsis.test.redis

import kotlinx.coroutines.reactor.awaitSingle
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class TestResultPublisher @Autowired constructor(
  @Value("{STREAM_KEY_TEST_RESULT}") streamKey: String,
  redis: ReactiveRedisTemplate<String, String>
) : RedisStreamProducer(streamKey, redis) {

  suspend fun publishTestResult(testId: String) {
    emit("Test with ID $testId completed").awaitSingle()
  }
}
