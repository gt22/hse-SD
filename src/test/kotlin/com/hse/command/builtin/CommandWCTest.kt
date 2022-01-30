package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream


internal class CommandWCTest {

    @Test
    fun execute() {
        val input = "".byteInputStream()
        val outputStream = ByteArrayOutputStream()
        val shell = Shell(input, outputStream)
        val fileName = "src/test/resources/testFile.txt"
        shell.execute("wc $fileName")
        Assertions.assertEquals("3 4 8 $fileName" + System.lineSeparator(), outputStream.toString())
    }
}