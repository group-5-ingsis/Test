package com.ingsis.test.tests

data class TestDto(
  val id: String?,
  val name: String,
  val snippetAuthor: String,
  val input: List<String>?,
  val output: List<String>?
)
