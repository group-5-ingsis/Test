package com.ingsis.test.tests

import com.ingsis.test.config.JwtInfoExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController("/test")
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

  @PostMapping("/delete/{testId}")
  fun deleteTest(@PathVariable testId: String) {
    testRepository.deleteById(testId)
  }

  @PostMapping("/run/{testId}")
  suspend fun runTest(@AuthenticationPrincipal jwt: Jwt, @PathVariable testId: String) {
    val userData = JwtInfoExtractor.createUserData(jwt)
    testService.runTest(userData, testId)
  }

  @PostMapping("/run/{snippetId}/all")
  suspend fun runAllTestsForSnippet(@AuthenticationPrincipal jwt: Jwt, @PathVariable snippetId: String) {
    val userData = JwtInfoExtractor.createUserData(jwt)
    val tests = withContext(Dispatchers.IO) {
      testRepository.findAllBySnippetId(snippetId)
    }
    tests.forEach { test ->
      testService.runTest(userData, test.id)
    }
  }
}
