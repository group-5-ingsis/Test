package com.ingsis.test.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*

@RestController
class TestController(
  private val testRepository: TestRepository,
  private val testService: TestService
) {

  @PostMapping("/{snippetId}")
  fun createTest(@PathVariable snippetId: String, @RequestBody testDto: TestDto): TestDto {
    val test = Test(testDto, snippetId)
    testRepository.save(test)
    return testDto
  }

  @GetMapping("/{snippetId}")
  fun getAllTestsForSnippet(@PathVariable snippetId: String): List<Test> {
    return testRepository.findAllBySnippetId(snippetId)
  }

  @GetMapping("/test")
  fun getTest(): String {
    return "Hello World"
  }

  @PostMapping("/{testId}")
  fun deleteTest(@PathVariable testId: String) {
    testRepository.deleteById(testId)
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
