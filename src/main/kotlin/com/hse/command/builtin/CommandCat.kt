package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Paths

class CommandCat : SimpleCommand("cat") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        if (arguments.isEmpty()) {
            while (true) {
                val line = ctx.reader.readLine() ?: break
                ctx.writer.println(line)
            }
        } else {
            for (argument in arguments) {
                val file = ctx.resolvePath(Paths.get(argument)).toFile()
                ctx.writer.println(String((file.readBytes())))
            }
        }
    }
}