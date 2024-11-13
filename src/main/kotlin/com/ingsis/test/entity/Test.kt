package com.ingsis.test.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Test {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private var id: Long = 0

  @Column(name = "snippetId")
  private var snippetId: Long = 0

  @Column(name = "userInputs")
  @ElementCollection
  private var userInputs: List<String> = mutableListOf()

  @Column(name = "userOutputs")
  @ElementCollection
  private var userOutputs: List<String> = mutableListOf()

  @Column(name = "testPassed")
  private var testPassed: Boolean = false
}
