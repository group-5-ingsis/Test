package com.ingsis.test.result

data class TestResult(
  val testId: String,
  val result: TestStatus
)

enum class TestStatus {
  PENDING,
  PASSED,
  FAILED
}
