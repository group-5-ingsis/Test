package com.ingsis.test.languages

interface Language {

  fun execute(src: String, version: String, input: String): String

}
