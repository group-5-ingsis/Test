package com.ingsis.test.tests

import com.ingsis.test.dto.CreateTestDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/test")
class TestController {

  @Autowired
  private lateinit var testService: TestService

  @PostMapping("/")
  fun createTest(@RequestBody request: CreateTestDto): Test {
    return testService.createTest(request)
  }

  @PostMapping("/{snippetId}/run")
  fun runTest(@PathVariable snippetId: String): Boolean {
    return testService.runTestsForSnippet(snippetId)
  }

}
