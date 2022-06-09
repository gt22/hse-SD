package com.hse.command.builtin

import org.junit.jupiter.api.Assertions.*
import java.nio.charset.StandardCharsets.UTF_8

internal class CommandGrepTest {
    @org.junit.jupiter.api.Test
    fun execute() {
        val result = testCommand(javaClass.getResourceAsStream("/testFile.txt")!!) { ctx ->
            CommandGrep().execute("grep", listOf("1"), ctx)
        }
        assertEquals("1 2", result)
    }

    @org.junit.jupiter.api.Test
    fun `test afterContext flag`() {
        val text = javaClass.getResourceAsStream("/test")!!.readAllBytes().toString(UTF_8)
        val result = testCommand(javaClass.getResourceAsStream("/test")!!) { ctx ->
            CommandGrep().execute("grep", listOf("1", "-A", "3"), ctx)
        }
        assertEquals(text, result)
    }

    @org.junit.jupiter.api.Test
    fun `test -w flag`() {
        val result = testCommand(javaClass.getResourceAsStream("/testW")!!) { ctx ->
            CommandGrep().execute("grep", listOf("23", "-w"), ctx)
        }
        assertEquals("23\n" +
                "23!\n" +
                "!23", result)
    }
}