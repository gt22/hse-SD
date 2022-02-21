package com.hse.command.builtin

import org.junit.jupiter.api.Assertions.*

internal class CommandGrepTest {
    @org.junit.jupiter.api.Test
    fun execute() {
        val result = testCommand(javaClass.getResourceAsStream("/testFile.txt")!!) { ctx ->
            CommandGrep().execute("grep", listOf("1"), ctx)
        }
        assertEquals("1 2", result)
    }
}