package com.ingsis.test.redis

import com.ingsis.test.asset.Asset
import com.ingsis.test.asset.AssetService
import com.ingsis.test.dto.CreateTestDto
import com.ingsis.test.tests.Test
import com.ingsis.test.tests.TestRepository
import com.ingsis.test.utils.JsonUtils
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
class CreateTestRequestConsumer @Autowired constructor(
  redis: ReactiveRedisTemplate<String, String>,
  @Value("\${stream.test-create}") streamKey: String,
  @Value("\${groups.test-create}") groupId: String,
  private val assetService: AssetService,
  private val testRepository: TestRepository
) : RedisStreamConsumer<String>(streamKey, groupId, redis) {

  override fun onMessage(record: ObjectRecord<String, String>) {
    val streamValue = record.value
    val test = JsonUtils.deserializeTestRequest(streamValue)
    testRepository.save(test)
  }

  override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>> {
    return StreamReceiver.StreamReceiverOptions.builder()
      .targetType(String::class.java)
      .build()
  }

}
