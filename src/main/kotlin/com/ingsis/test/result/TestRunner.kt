package com.ingsis.test.result

import com.ingsis.test.asset.AssetService
import com.ingsis.test.config.UserData
import com.ingsis.test.languages.PrintScript
import com.ingsis.test.tests.Test
import org.springframework.stereotype.Component

@Component
class TestRunner(private val assetService: AssetService) {

  fun runTest(userData: UserData, test: Test): TestResult {
    val language = PrintScript
    val snippet = assetService.getAssetContent(userData.username, test.snippetId)
    val inputs = test.userInputs
    val outputs = test.userOutputs
    inputs.forEach { input ->
      val executionResult = language.execute(snippet, "1.1", input)
      if (executionResult != outputs[inputs.indexOf(input)]) {
        return TestResult(test.id, TestStatus.PASSED)
      }
    }
    return TestResult(test.id, TestStatus.FAILED)
  }
}
