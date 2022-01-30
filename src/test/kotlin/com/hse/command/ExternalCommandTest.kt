package com.hse.command

import com.hse.command.builtin.testCommand
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.InetAddress

internal class ExternalCommandTest {
    @Test
    fun execute() {
        val result = testCommand { ctx ->
            ExternalCommand().execute("hostname", emptyList(), ctx)
        }
        Assertions.assertEquals(InetAddress.getLocalHost().hostName, result)
    }
}