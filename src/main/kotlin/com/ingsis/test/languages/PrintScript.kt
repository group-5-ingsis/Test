package com.ingsis.test.languages

import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import java.io.IOException

object PrintScript : Language {

  override fun execute(src: String, version: String, input: String): String {
    val output = StringBuilder()
    try {
      val tokens = Lexer(src, version)
      val asts = Parser(tokens, version, null)
      val interpreter = Interpreter
      val result = interpreter.interpret(asts.next(), version, asts.getEnv(), input)
      output.append(result.first)
    } catch (e: IOException) {
      System.err.println("I/O Error: ${e.message}")
    } catch (e: Exception) {
      System.err.println("Error: ${e.message}")
    } finally {
      try {
      } catch (e: IOException) {
        System.err.println("Error closing writer: ${e.message}")
      }
    }
    return output.toString()
  }
}
