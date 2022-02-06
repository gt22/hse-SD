package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Files
import java.nio.file.Paths

class CommandCat : SimpleCommand("cat") {
    // prints content of given files or given input
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        if (arguments.isEmpty()) {
            ctx.input.transferTo(ctx.output)
        } else {
            for (argument in arguments) {
                val file = ctx.shell.resolvePath(Paths.get(argument))
                ctx.output.write(Files.readAllBytes(file))
            }
        }
        return 0
    }
}