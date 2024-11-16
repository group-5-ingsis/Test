package com.ingsis.test.tests

import com.ingsis.test.dto.CreateTestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.*

@RestController("/test")
class TestController(
  private val testRepository: TestRepository,
  private val testService: TestService) {

  @PostMapping("/create")
  fun createTest(@RequestBody createTestDto: CreateTestDto) {
    val test = Test(createTestDto)
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
