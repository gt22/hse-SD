package com.hse

import com.hse.command.builtin.*
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.nio.file.Path
import java.nio.file.Paths

class CommandContext(input: InputStream = System.`in`, output: OutputStream = System.out) {
    private val shell = Shell(this)
    val simpleCommands = listOf(CommandCat(), CommandEcho(), CommandExit(), CommandPWD(), CommandWC())
    val writer = PrintWriter(output.writer(), true)
    val reader = input.bufferedReader()

    val workingDirectoryAbsolutePath: Path = Paths.get("").toAbsolutePath()

    fun resolvePath(path: Path): Path = if (path.isAbsolute) path else workingDirectoryAbsolutePath.resolve(path)

    fun startShell() {
        while (true) {
            val line = reader.readLine() ?: break
            try {
                shell.execute(line)
            } catch (_: ExitException) {
                break
            } catch (e: Throwable) {
                writer.println(e.toString())
            }
        }
        reader.close()
        writer.close()
    }
}
