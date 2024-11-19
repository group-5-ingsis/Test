package com.ingsis.test.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class TestController(
  private val testRepository: TestRepository,
  private val testService: TestService
) {

  private val logger = LoggerFactory.getLogger(TestController::class.java)

  @PostMapping("/{snippetId}")
  fun createTest(@PathVariable snippetId: String, @RequestBody testDto: TestDto): TestDto {
    val test = Test(testDto, snippetId)
    testRepository.save(test)
    logger.info("Test created with id: ${test.id}")
    return testDto
  }

  @GetMapping("/{snippetId}")
  fun getAllTestsForSnippet(@PathVariable snippetId: String): List<Test> {
    logger.info("Received request to get all tests for snippet with id: $snippetId")
    return testRepository.findAllBySnippetId(snippetId)
  }

  @GetMapping("/test")
  fun getTest(): String {
    return "Hello World"
  }

  @DeleteMapping("/{testId}")
  fun deleteTest(@PathVariable testId: String) {
    testRepository.deleteById(testId)
    logger.info("Removed test with id: $testId")
  }

  @PostMapping("/test/{testId}")
  suspend fun runTest(@PathVariable testId: String) {
    testService.runTest(testId)
  }

  @PostMapping("/test/{snippetId}/all")
  suspend fun runAllTestsForSnippet(@PathVariable snippetId: String) {
    val tests = withContext(Dispatchers.IO) {
      testRepository.findAllBySnippetId(snippetId)
    }
    tests.forEach { test ->
      testService.runTest(test.id)
    }
  }
}
