package com.hse.command

import com.hse.Shell
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class ExternalCommandTest {
    @Test
    fun execute() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        shell.execute("ls src")
        Assertions.assertEquals("main" + System.lineSeparator() +
                "test" + System.lineSeparator(), outputStream.toString())
    }
}