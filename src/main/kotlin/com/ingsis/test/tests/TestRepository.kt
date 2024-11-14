package com.ingsis.test.tests

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TestRepository : JpaRepository<Test, String> {
    fun findAllBySnippetId(snippetId: String): List<Test>
}
