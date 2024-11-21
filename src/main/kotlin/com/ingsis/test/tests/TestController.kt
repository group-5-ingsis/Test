package com.ingsis.test.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TestController(
  private val testRepository: TestRepository,
  private val testService: TestService
) {

  private val logger = LoggerFactory.getLogger(TestController::class.java)

  @PostMapping("/{snippetId}")
  fun createTest(@PathVariable snippetId: String, @RequestBody testDto: TestDto): ResponseEntity<TestDto> {
    val test = Test(testDto, snippetId)
    testRepository.save(test)
    logger.info("Test created with id: ${test.id}")
    return ResponseEntity.ok(testDto)
  }

  @GetMapping("/{snippetId}")
  fun getAllTestsForSnippet(@PathVariable snippetId: String): ResponseEntity<List<Test>> {
    logger.info("Received request to get all tests for snippet with id: $snippetId")
    val tests = testRepository.findAllBySnippetId(snippetId)
    return ResponseEntity.ok(tests)
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
    logger.info("Received request to run test with id: $testId")
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
