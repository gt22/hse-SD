package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class CommandPWDTest {
    @Test
    fun execute() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        shell.execute("pwd")
        val path = System.getProperty("user.dir")

        assertEquals(
            path + System.lineSeparator(), outputStream.toString()
        )
    }
}