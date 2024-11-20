package com.ingsis.test.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ingsis.test.result.TestResult
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

  fun deserializeTestRequest(json: String): Test {
    return try {
      objectMapper.readValue(json, Test::class.java)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to deserialize JSON to object", e)
    }
  }

  fun serializeTestResult(testResult: TestResult): String {
    return try {
      objectMapper.writeValueAsString(testResult)
    } catch (e: JsonProcessingException) {
      throw RuntimeException("Failed to serialize object to JSON", e)
    }
  }
}
