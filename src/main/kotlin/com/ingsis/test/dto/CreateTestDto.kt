package com.ingsis.test.dto

data class CreateTestDto(
    val snippetId: String,
    val author: String,
    val name: String,
    val userInputs: List<String>,
    val userOutputs: List<String>,
    val language: String,
    val version: String,
    val testPassed: Boolean,
)
