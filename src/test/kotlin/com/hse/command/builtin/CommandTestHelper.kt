package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.Shell
import com.hse.command.ExternalCommand
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

fun testCompareWithExternal(command: String, arguments: List<String>, getStream: () -> InputStream) {
    val expected = testCommand(getStream()) { ctx ->
        ExternalCommand().execute(command, arguments, ctx)
    }
    val result = testCommand(getStream()) { ctx ->
        CommandGrep().execute(command, arguments, ctx)
    }
    assertEquals(expected, result)
}

val ln = System.lineSeparator()

fun testCommand(input: String = "", expectedExitCode: Int = 0, act: (CommandContext) -> Int) =
    testCommand(input.byteInputStream(), expectedExitCode, act)