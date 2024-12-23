package com.ingsis.test.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory

@Configuration
open class ConnectionFactory(@Value("\${spring.data.redis.host}") private val hostName: String, @Value("\${spring.data.redis.port}") private val port: Int) {

  @Bean
  open fun redisConnectionFactory(): LettuceConnectionFactory {
    return LettuceConnectionFactory(
      RedisStandaloneConfiguration(hostName, port)
    )
  }
}
