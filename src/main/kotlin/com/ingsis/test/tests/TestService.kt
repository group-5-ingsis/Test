package com.ingsis.test.tests

import com.ingsis.test.result.TestResultProducer
import com.ingsis.test.result.TestRunner
import com.ingsis.test.result.TestStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TestService(
  private val testRepository: TestRepository,
  private val testResultProducer: TestResultProducer,
  private val testRunner: TestRunner
) {
  private val logger = LoggerFactory.getLogger(TestService::class.java)

  suspend fun runTest(testId: String) {
    val testCall = testRepository.findById(testId)
    val test = testCall.get()

    try {
      val result = testRunner.runTest(test)
      logger.info("Test result: ${result.result}")

      test.testPassed = if (result.result == TestStatus.PASSED) TestStatus.PASSED else TestStatus.FAILED

      withContext(Dispatchers.IO) {
        testRepository.save(test)
      }

      testResultProducer.publishEvent(result)
      logger.info("Test result '${result.result}' for test id '$testId' saved and published.")
    } catch (e: Exception) {
      logger.info("Test running for test id '$testId' raised an exception. ")
      test.testPassed = TestStatus.FAILED
      withContext(Dispatchers.IO) {
        testRepository.save(test)
      }
    }
  }
}
