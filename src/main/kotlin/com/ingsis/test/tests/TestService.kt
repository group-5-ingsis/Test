package com.ingsis.test.tests

import com.ingsis.test.result.TestResultProducer
import com.ingsis.test.result.TestRunner
import com.ingsis.test.result.TestStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class TestService(
  private val testRepository: TestRepository,
  private val testResultProducer: TestResultProducer,
  private val testRunner: TestRunner
) {
  suspend fun runTest(testId: String) {
    val test = withContext(Dispatchers.IO) {
      testRepository.findById(testId)
    }.get()
    val result = testRunner.runTest(test)
    test.testPassed = if result.result == TestStatus.PASSED then true
    withContext(Dispatchers.IO) {
      testRepository.save(test)
    }
    testResultProducer.publishEvent(result)
  }
}
