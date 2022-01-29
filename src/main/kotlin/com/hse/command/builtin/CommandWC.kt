package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Paths

class CommandWC: SimpleCommand("wc") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        for (argument in arguments) {
            val file = ctx.resolvePath(Paths.get(argument)).toFile()
            val text = file.readText()
            ctx.writer.println("${text.lines().size} ${text.trim().split("\\s+".toRegex()).size} ${text.length} $argument")
        }
    }
}