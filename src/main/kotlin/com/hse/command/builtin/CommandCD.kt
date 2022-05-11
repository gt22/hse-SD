package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Path
import kotlin.io.path.isDirectory

/**
 * Класс, реализующий встроенную команду изменения текущей директории.
 * При пустых аргументах, изменяет текущую директорию на домашнюю.
 * Иначе делает resolve относительно предыдущего значения.
 */
class CommandCD : SimpleCommand("cd") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        if (arguments.size > 1) {
            ctx.writer.println("cd: too many arguments")
            return 1
        }
        val diffPath = Path.of(arguments.firstOrNull() ?: System.getProperty("user.home"))
        val newPath = ctx.shell.workingDirectory.resolve(diffPath)
        if (!newPath.isDirectory()) {
            ctx.writer.println("cd: $diffPath: No such directory")
            return 1
        }
        ctx.shell.workingDirectory = newPath
        return 0
    }
}
