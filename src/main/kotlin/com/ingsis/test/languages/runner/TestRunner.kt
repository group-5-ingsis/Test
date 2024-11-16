package com.ingsis.test.languages.runner

import com.ingsis.test.asset.AssetService
import com.ingsis.test.config.RestTemplateConfig
import com.ingsis.test.languages.LanguageProvider
import com.ingsis.test.tests.Test

object TestRunner {
  private val assetService = AssetService(RestTemplateConfig().restTemplate())

  fun runTest(test: Test) : TestResult {
    val language = LanguageProvider.getLanguages()[test.language]
    val snippet = assetService.getAssetContent(test.author, test.snippetId)
    val inputs = test.userInputs
    val outputs = test.userOutputs
    if (language != null) {
      inputs.forEach { input ->
        val executionResult = language.execute(snippet, test.version, input)
        if (executionResult != outputs[inputs.indexOf(input)]) {
          return TestResult(test.id, false)
        }
      }
    } else {
      throw Exception("Language not found")
    }
    return TestResult(test.id, true)
  }
}
