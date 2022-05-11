package com.hse.command.builtin

import com.hse.Shell
import com.hse.command.ExternalCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolute

internal class CommandCDTest {
    @Test
    fun `cd empty`() {
        val result = testCommand { ctx ->
            val rv = CommandCD().execute("cd", emptyList(), ctx)
            assertEquals(Path.of(System.getProperty("user.home")), ctx.shell.workingDirectory)
            return@testCommand rv
        }
        assertEquals("", result)
    }

    @Test
    fun `cd backward`() {
        val result = testCommand { ctx ->
            val rv = CommandCD().execute("cd", listOf(".."), ctx)
            assertEquals(Path.of(".").absolute().normalize().parent, ctx.shell.workingDirectory)
            return@testCommand rv
        }
        assertEquals("", result)
    }

    @Test
    fun `cd forward`() {
        val subdirectory = File(".").listFiles(File::isDirectory)?.get(0)!!
        val result = testCommand { ctx ->
            val rv = CommandCD().execute("cd", listOf(subdirectory.name), ctx)
            assertEquals(Path.of(subdirectory.path).absolute().normalize(), ctx.shell.workingDirectory)
            return@testCommand rv
        }
        assertEquals("", result)
    }

    @Test
    fun `cd external`() {
        val shell = Shell(emptyList())
        val pwdBefore = testCommand(shell) { ctx ->
            ExternalCommand().execute("pwd", emptyList(), ctx)
        }
        assertEquals(shell.workingDirectory, Path.of(pwdBefore))
        val cdResult = testCommand(shell) { ctx ->
            CommandCD().execute("cd", listOf(".."), ctx)
        }
        assertEquals("", cdResult)
        val pwdAfter = testCommand(shell) { ctx ->
            ExternalCommand().execute("pwd", emptyList(), ctx)
        }
        assertEquals(shell.workingDirectory, Path.of(pwdAfter))
    }

    @Test
    fun `cd absolute`() {
        val subdirectory = File(".").listFiles(File::isDirectory)?.get(0)!!
        val result = testCommand { ctx ->
            val rv = CommandCD().execute("cd", listOf(subdirectory.absolutePath), ctx)
            assertEquals(Path.of(subdirectory.path).absolute().normalize(), ctx.shell.workingDirectory)
            return@testCommand rv
        }
        assertEquals("", result)
    }
}
