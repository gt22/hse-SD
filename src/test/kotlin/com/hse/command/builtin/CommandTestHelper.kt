package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.Shell
import java.io.ByteArrayOutputStream
import java.io.InputStream
import org.junit.jupiter.api.Assertions.assertEquals

val ln: String = System.lineSeparator()

fun testCommand(input: InputStream, expectedExitCode: Int = 0, act: (CommandContext) -> Int): String {
    val shell = Shell(emptyList())
    return testCommand(shell, input, expectedExitCode, act)
}

private fun testCommand(
    shell: Shell,
    input: InputStream,
    expectedExitCode: Int = 0,
    act: (CommandContext) -> Int
): String {
    ByteArrayOutputStream().use { output ->
        input.use { input ->
            val exitCode = act(CommandContext(shell, input, output))
            assertEquals(expectedExitCode, exitCode)
            return output.toString().removeSuffix(ln)
        }
    }
}

fun testCommand(input: String = "", expectedExitCode: Int = 0, act: (CommandContext) -> Int) =
    testCommand(input.byteInputStream(), expectedExitCode, act)

fun testCommand(shell: Shell, input: String = "", expectedExitCode: Int = 0, act: (CommandContext) -> Int) =
    testCommand(shell, input.byteInputStream(), expectedExitCode, act)
