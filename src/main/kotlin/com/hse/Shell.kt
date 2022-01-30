package com.hse

import com.hse.command.builtin.*
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.Paths

class Shell(val input: InputStream = System.`in`, val output: OutputStream = System.out) {
//    val environment: Map<String, String> = mapOf()
    val simpleCommands = listOf(CommandCat(), CommandEcho(), CommandExit(), CommandPWD(), CommandWC())
    private val parser = Parser(simpleCommands)

    fun execute(line: String): Int {
        val commandPipeline = parser.parseWithSubstitution(line)
        val ctx = CommandContext(this, input, output)
        commandPipeline.execute(ctx)

        return 0
    }

    fun startShell() {
        input.bufferedReader().use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                try {
                    execute(line)
                } catch (_: ExitException) {
                    break
                } catch (e: Throwable) {
                    output.write(e.localizedMessage.toByteArray())
                }
            }
        }
    }

    val workingDirectoryAbsolutePath: Path = Paths.get("").toAbsolutePath()
    fun resolvePath(path: Path): Path = if (path.isAbsolute) path else workingDirectoryAbsolutePath.resolve(path)
}