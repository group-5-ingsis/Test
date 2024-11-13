package com.ingsis.test.utils

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

object JsonUtils {
  private val objectMapper = ObjectMapper()
  fun serializeToJson(testToFormat: ): String {
    return try {
      objectMapper.writeValueAsString(snippetToFormat)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }
}
