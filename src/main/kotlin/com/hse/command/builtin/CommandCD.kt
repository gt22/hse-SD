package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Path

/**
 * Класс, реализующий встроенную команду изменения текущей директории.
 * При пустых аргументах, изменяет текущую директорию на домашнюю.
 * Иначе делает resolve относительно предыдущего значения.
 */
class CommandCD : SimpleCommand("cd") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        val path = Path.of(arguments.firstOrNull() ?: System.getProperty("user.home"))
        ctx.shell.workingDirectoryAbsolutePath = ctx.shell.workingDirectoryAbsolutePath.resolve(path)
        return 0
    }
}
