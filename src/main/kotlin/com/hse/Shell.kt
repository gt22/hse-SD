package com.hse

import com.hse.command.ICommand
import com.hse.command.builtin.*
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.absolute

/**
 * Основной класс CLI, хранит переменные среды и рабочую директорию
 * @param builtinCommands - встроенные команды, см. [Parser]
 * @param input - основной поток ввода
 * @param output - основной поток вывода
 */
class Shell(
    builtinCommands: List<ICommand>,
    val input: InputStream = System.`in`,
    val output: OutputStream = System.out
) {
    val environment: MutableMap<String, String> = System.getenv().toMutableMap()
    private val parser = Parser(this, builtinCommands)

    private fun execute(line: String): Int {
        val command = try {
            parser.parseWithSubstitution(line) ?: return 0
        } catch (e: IllegalArgumentException) {
            output.write("Parse error: ${e.message}\n".toByteArray())
            return 130
        }
        val ctx = CommandContext(this, input, output)
        return try {
            command.execute(ctx)
        } catch (e: ExitException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace() //TODO: Better logging?
            255
        }
    }

    /**
     * Запускает главный цикл CLI
     */
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

    var workingDirectory: Path = Path.of(".").absolute().normalize()
        set(newPath) {
            field = newPath.absolute().normalize()
        }

    /**
     * Возвращает путь с учётом рабочей директории
     * Не изменяет абсолютные пути
     */
    fun resolvePath(path: Path): Path = if (path.isAbsolute) path else workingDirectory.resolve(path)
}