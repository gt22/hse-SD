package com.hse

import com.hse.command.AbstractCommand
import com.hse.command.builtin.ExitException
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.Paths

class Shell(
    private val builtinCommands: List<AbstractCommand>,
    val input: InputStream = System.`in`,
    val output: OutputStream = System.out
) {
    val environment: MutableMap<String, String> = mutableMapOf()
    private val parser = Parser(builtinCommands)

    // executes given line
    fun execute(line: String): Int {
        val command = parser.parseWithSubstitution(line) ?: return 0
        val ctx = CommandContext(this, input, output)
        return try {
            command.execute(ctx)
        } catch (e: ExitException) {
            throw e
        } catch(e: Exception) {
            e.printStackTrace() //TODO: Better logging?
            255
        }
    }

    // starts reading input
    // passes every line to execute function
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