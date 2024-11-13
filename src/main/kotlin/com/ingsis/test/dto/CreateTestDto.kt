package com.ingsis.test.dto

data class CreateTestDto(
    val snippetId: String,
    val userInputs: List<String>,
    val userOutputs: List<String>,
    val testPassed: Boolean
)
