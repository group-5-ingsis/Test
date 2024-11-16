package com.ingsis.test.tests

import com.ingsis.test.languages.runner.TestRunner
import com.ingsis.test.redis.producer.TestResultProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service

@Service
class TestService(
  private val testRepository: TestRepository,
  private val testResultProducer: TestResultProducer
) {
  suspend fun runTest(testId: String) {
    val test = withContext(Dispatchers.IO) {
      testRepository.findById(testId)
    }.get()
    val result = TestRunner.runTest(test)
    test.testPassed = result.passed
    withContext(Dispatchers.IO) {
      testRepository.save(test)
    }
    testResultProducer.publishEvent(result)
  }
}
