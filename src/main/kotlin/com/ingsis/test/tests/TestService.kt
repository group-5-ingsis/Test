package com.ingsis.test.tests

import com.ingsis.test.dto.CreateTestDto
import com.ingsis.test.redis.TestResultPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TestService @Autowired constructor(
  private val testRepository: TestRepository,
  private val redisStreamPublisher: TestResultPublisher
) {

  fun createTest(request: CreateTestDto): Test {
    val test = Test(
      snippetId = request.snippetId,
      userInputs = request.userInputs,
      userOutputs = request.userOutputs,
      testPassed = request.testPassed
    )
    return testRepository.save(test)
  }

  fun runTestsForSnippet(snippetId: String): Boolean {
    val tests: List<Test> = testRepository.findAllBySnippetId(snippetId)
    // TODO: Add logic for PrintScript running
    tests.forEach { test ->
      // TODO: Add logic for comparing userOutputs with expected outputs
      test.testPassed = false
      testRepository.save(test)
      /* Publish an event each time a test finishes running. */
      redisStreamPublisher.publishTestResult("testStream", "Test with ID ${test.id} completed")
    }
    return tests.all { it.testPassed }
  }
}
