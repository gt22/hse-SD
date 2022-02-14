package com.hse.command.builtin

import com.hse.Shell
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream


internal class CommandWCTest {

    @Test
    fun execute() {
        val result = testCommand(javaClass.getResourceAsStream("/testFile.txt")!!) { ctx ->
            CommandWC().execute("wc", emptyList(), ctx)
        }
        Assertions.assertEquals("3 4 8", result)
    }
}