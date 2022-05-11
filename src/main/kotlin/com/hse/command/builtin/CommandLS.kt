package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries

/**
 * Класс, реализующий встроенную команду вывода содержимого.
 * При отсутствии аргумента выводит содержимое текущей директории.
 * При переданном аргументе выводит содержимое директории, название которой берется из
 * переданного аргумента.
 * Если в качестве аргумента передано название файла, то будет выведено название этого файла.
 */
class CommandLS : SimpleCommand("ls") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        if (arguments.size > 1) {
            ctx.writer.println("ls: too many arguments")
            return 1
        }
        val path = ctx.shell.resolvePath(Path.of(arguments.firstOrNull() ?: ""))
        val content = if (path.isDirectory()) {
            path.listDirectoryEntries().map { it.fileName }.sortedBy { it.toString().lowercase() }
                    .joinToString(separator = System.lineSeparator())
        } else if (path.isRegularFile()) {
            arguments[0]
        } else {
            ctx.writer.println("ls: ${path.fileName}: No such directory")
            return 1
        }
        ctx.writer.println(content)
        return 0
    }
}
