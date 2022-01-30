package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class CommandCatTest {
    @Test
    fun execute() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        val fileName = "src/test/resources/testFile.txt"
        shell.execute("cat $fileName")
        assertEquals(
            "1 2" + System.lineSeparator() +
                    "3 4" + System.lineSeparator(), outputStream.toString()
        )
    }
}