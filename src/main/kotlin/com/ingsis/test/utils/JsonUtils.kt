package com.ingsis.test.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ingsis.test.tests.Test

object JsonUtils {
  private val objectMapper = jacksonObjectMapper()

  fun serializeToJson(testToFormat: Test): String {
    return try {
      objectMapper.writeValueAsString(testToFormat)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }
}