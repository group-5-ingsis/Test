package com.ingsis.test.tests

import com.ingsis.test.dto.CreateTestDto
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
data class Test(
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String = UUID.randomUUID().toString(),

  @Column(name = "snippetId")
  var snippetId: String,

  @Column(name = "name")
  var name: String,

  @Column(name = "userInputs")
  @ElementCollection
  var userInputs: List<String>,

  @Column(name = "userOutputs")
  @ElementCollection
  var userOutputs: List<String>,

  @Column(name = "language")
  var language: String,

  @Column(name = "version")
  var version: String,

  @Column(name = "testPassed")
  var testPassed: Boolean,
) {
  constructor() : this(
    id = UUID.randomUUID().toString(),
    snippetId = "",
    name = "",
    userInputs = mutableListOf(),
    userOutputs = mutableListOf(),
    language = "",
    version = "",
    testPassed = false
  )

  constructor(createTestDto: CreateTestDto) : this(
    id = UUID.randomUUID().toString(),
    snippetId = createTestDto.snippetId,
    name = createTestDto.name,
    userInputs = createTestDto.userInputs,
    userOutputs = createTestDto.userOutputs,
    language = createTestDto.language,
    version = createTestDto.version,
    testPassed = createTestDto.testPassed
  )
}
