package com.ingsis.test.tests

import com.ingsis.test.result.TestStatus
import jakarta.persistence.*
import java.util.UUID

@Entity
data class Test(

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  var id: String,

  var snippetId: String,

  var name: String,

  var snippetAuthor: String,

  @ElementCollection
  var userInputs: List<String>,

  @ElementCollection
  var userOutputs: List<String>,

  @Enumerated(EnumType.STRING)
  var testPassed: TestStatus
) {

  constructor(testDto: TestDto, snippetId: String) : this(
    id = UUID.randomUUID().toString(),
    snippetId = snippetId,
    snippetAuthor = "",
    name = testDto.name,
    userInputs = testDto.input,
    userOutputs = testDto.output,
    testPassed = TestStatus.PENDING
  )
}
