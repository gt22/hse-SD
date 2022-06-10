package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class CommandPWDTest {
    @Test
    fun execute() {
        val result = testCommand { ctx ->
            CommandPWD().execute("pwd", emptyList(), ctx)
        }
        val path = System.getProperty("user.dir")
        assertEquals(path, result)
    }
}