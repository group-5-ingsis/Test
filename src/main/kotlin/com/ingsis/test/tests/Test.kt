package com.ingsis.test.tests

import com.ingsis.test.result.TestStatus
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
  var id: String,

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

  @Column(name = "testPassed")
  var testPassed: TestStatus
) {

  constructor(testDto: TestDto, snippetId: String) : this(
    id = UUID.randomUUID().toString(),
    snippetId = snippetId,
    name = testDto.name,
    userInputs = testDto.input,
    userOutputs = testDto.output,
    testPassed = TestStatus.PENDING
  )
}
