package com.ingsis.test.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*

@RestController("/service/test")
class TestController(
  private val testRepository: TestRepository,
  private val testService: TestService
) {

  @PostMapping("/{snippetId}")
  fun createTest(@PathVariable snippetId: String, @RequestBody testDto: TestDto) {
    val test = Test(testDto, snippetId)
    testRepository.save(test)
  }

  @GetMapping("/{snippetId}")
  fun getAllTestsForSnippet(@PathVariable snippetId: String): List<Test> {
    return testRepository.findAllBySnippetId(snippetId)
  }

  @PostMapping("/delete/{testId}")
  fun deleteTest(@PathVariable testId: String) {
    testRepository.deleteById(testId)
  }

  @PostMapping("/run/{testId}")
  suspend fun runTest(@PathVariable testId: String) {
    /* TODO: Verify if has permission to run. */
    testService.runTest(testId)
  }

  @PostMapping("/run/{snippetId}/all")
  suspend fun runAllTestsForSnippet(@PathVariable snippetId: String) {
    val tests = withContext(Dispatchers.IO) {
      testRepository.findAllBySnippetId(snippetId)
    }
    tests.forEach({ test ->
      testService.runTest(test.id)
    })
  }
}
