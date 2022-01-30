package com.hse.command

import com.hse.Shell
import com.hse.command.builtin.testCommand
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

internal class ExternalCommandTest {
    @Test
    fun execute() {
        val result = testCommand { ctx ->
            ExternalCommand().execute("date", listOf("-r", "1643577487", "-u", "+%Y-%m-%dT%H:%M:%S%Z"), ctx)
        }
        Assertions.assertEquals("2022-01-30T21:18:07UTC", result)
    }
}