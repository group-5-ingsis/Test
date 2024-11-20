package com.ingsis.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class Test

fun main(args: Array<String>) {
  runApplication<Test>(*args)
}
