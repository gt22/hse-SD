package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.Shell
import java.io.ByteArrayOutputStream
import java.io.InputStream
import org.junit.jupiter.api.Assertions.assertEquals

fun testCommand(input: InputStream, expectedExitCode: Int = 0, act: (CommandContext) -> Int): String {
    val shell = Shell(emptyList())
    ByteArrayOutputStream().use { output ->
        input.use { input ->
            val exitCode = act(CommandContext(shell, input, output))
            assertEquals(expectedExitCode, exitCode)
            return output.toString().removeSuffix(System.lineSeparator())
        }
    }
}

val ln = System.lineSeparator()

fun testCommand(input: String = "", expectedExitCode: Int = 0, act: (CommandContext) -> Int) =
    testCommand(input.byteInputStream(), expectedExitCode, act)