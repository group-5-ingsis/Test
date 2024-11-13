package com.ingsis.test.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveStreamOperations
import org.springframework.stereotype.Component

@Component
class RedisStreamPublisher @Autowired constructor(
    private val redisTemplate: ReactiveRedisTemplate<String, String>
) {
    private val streamOps: ReactiveStreamOperations<String, String, String> = redisTemplate.opsForStream()

    fun publish(stream: String, message: String) {
        streamOps.add(stream, message).subscribe()
    }
}
