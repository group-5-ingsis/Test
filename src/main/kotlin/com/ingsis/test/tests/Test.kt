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

  @Column(name = "userInputs")
  @ElementCollection
  var userInputs: List<String>,

  @Column(name = "userOutputs")
  @ElementCollection
  var userOutputs: List<String>,

  @Column(name = "testPassed")
  var testPassed: Boolean,
) {
  constructor() : this(
    id = UUID.randomUUID().toString(),
    snippetId = "",
    userInputs = mutableListOf(),
    userOutputs = mutableListOf(),
    testPassed = false
  )

  constructor(createTestDto: CreateTestDto) : this(
    id = UUID.randomUUID().toString(),
    snippetId = createTestDto.snippetId,
    userInputs = createTestDto.userInputs,
    userOutputs = createTestDto.userOutputs,
    testPassed = createTestDto.testPassed
  )
}
