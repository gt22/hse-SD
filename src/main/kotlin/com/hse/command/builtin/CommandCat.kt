package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import com.hse.writeln
import java.nio.file.Paths

class CommandCat : SimpleCommand("cat") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        if (arguments.isEmpty()) {
            ctx.input.transferTo(ctx.output)
        } else {
            for (argument in arguments) {
                val file = ctx.shell.resolvePath(Paths.get(argument)).toFile()
                ctx.output.writeln(file.readBytes())
            }
        }
    }
}