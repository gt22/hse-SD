package com.hse.command.builtin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CommandEchoTest {
    @Test
    fun execute() {
        val result = testCommand { ctx ->
            CommandEcho().execute("echo", listOf("some", "input"), ctx)
        }
        assertEquals("some input", result)
    }

}