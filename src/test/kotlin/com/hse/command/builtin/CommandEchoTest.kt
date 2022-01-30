package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class CommandEchoTest {
    @Test
    fun execute() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        shell.execute("echo some input")
        assertEquals("some input" + System.lineSeparator(), outputStream.toString())
    }

    @Test
    fun quotes() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        shell.execute("echo \"some \"\"input\"")
        assertEquals("some input" + System.lineSeparator(), outputStream.toString())
    }
}