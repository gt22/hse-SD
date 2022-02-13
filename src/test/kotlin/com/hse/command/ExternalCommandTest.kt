package com.hse.command

import com.hse.command.builtin.testCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.InetAddress

internal class ExternalCommandTest {
    @Test
    fun execute() {
        val result = testCommand { ctx ->
            ExternalCommand().execute("hostname", emptyList(), ctx)
        }
        assertEquals(InetAddress.getLocalHost().hostName, result)
    }

    @Test
    fun testPassInput() {
        val result = testCommand("asd") { ctx ->
            ExternalCommand().execute("grep", listOf("asd"), ctx)
        }
        assertEquals("asd", result)
    }
}