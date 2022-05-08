package com.hse.command.builtin

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import org.junit.jupiter.api.Assertions.assertEquals

internal class CommandLSTest {
    private val tmpDirName = "tmpDirWithUniqName"
    private val subDirName = "subDir"
    private val filesInTempDir = listOf("file")
    private val subDirsInTempDir = listOf(subDirName)
    private val filesInSubDir = listOf("fileInSubDir")

    @BeforeEach
    private fun `create test directory`() {
        val dir = File(tmpDirName).apply { mkdir() }
        for (fn in filesInTempDir)
            File(dir, fn).createNewFile()
        for (sbn in subDirsInTempDir)
            File(dir, sbn).apply { mkdir() }

        val subDir = File(dir, subDirName)
        for (fn in filesInSubDir)
            File(subDir, fn).createNewFile()
    }

    @AfterEach
    private fun `remove test directory`() {
        File(tmpDirName).deleteRecursively()
    }

    @Test
    fun `test no arguments`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv = CommandLS().execute("ls", emptyList(), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals((filesInTempDir + subDirsInTempDir).sortedBy { it.lowercase() }, result.lines())
    }

    @Test
    fun `test directory argument`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv = CommandLS().execute("ls", listOf(subDirName), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals(filesInSubDir.sortedBy { it.lowercase() }, result.lines())
    }

    @Test
    fun `test not exist directory argument`() {
        val notExistDirName = "jopjiorhgpregh"
        val result = testCommand(expectedExitCode = 1) { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv = CommandLS().execute("ls", listOf(notExistDirName), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals("ls: $notExistDirName: No such directory", result)
    }

    @Test
    fun `test file argument`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv = CommandLS().execute("ls", listOf(filesInTempDir[0]), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals(filesInTempDir[0], result)
    }

    @Test
    fun `test many arguments`() {
        val result = testCommand(expectedExitCode = 1) { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv = CommandLS().execute("ls", listOf(filesInTempDir[0], "second argument"), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals("ls: too many arguments", result)
    }

    @Test
    fun `test double dots argument`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf("$tmpDirName${File.separator}$subDirName"), ctx)
            val rv = CommandLS().execute("ls", listOf(".."), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals((filesInTempDir + subDirsInTempDir).sortedBy { it.lowercase() }, result.lines())
    }

    @Test
    fun `test full path argument `() {
        val result = testCommand { ctx ->
            val rv = CommandLS().execute(
                "ls",
                listOf("${System.getProperty("user.dir")}${File.separator}${tmpDirName}"),
                ctx
            )
            return@testCommand rv
        }
        assertEquals((filesInTempDir + subDirsInTempDir).sortedBy { it.lowercase() }, result.lines())
    }

    @Test
    fun `test double dots slash path`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            CommandCD().execute("cd", listOf(subDirName), ctx)
            val rv = CommandLS().execute("ls", listOf("..${File.separator}"), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals((filesInTempDir + subDirsInTempDir).sortedBy { it.lowercase() }, result.lines())
    }

    @Test
    fun `test path with many slashes at the end`() {
        val result = testCommand { ctx ->
            CommandCD().execute("cd", listOf(System.getProperty("user.dir")), ctx)
            CommandCD().execute("cd", listOf(tmpDirName), ctx)
            val rv =
                CommandLS().execute("ls", listOf("$subDirName${File.separator}${File.separator}${File.separator}"), ctx)
            CommandCD().execute("cd", listOf("..${File.separator}"), ctx)
            return@testCommand rv
        }
        assertEquals(filesInSubDir.sortedBy { it.lowercase() }, result.lines())
    }
}
