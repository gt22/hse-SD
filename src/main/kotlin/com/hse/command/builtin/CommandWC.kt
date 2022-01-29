package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import com.hse.writeln
import java.nio.file.Paths

class CommandWC : SimpleCommand("wc") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        if (arguments.isEmpty()) {
            printWCForText(ctx, String(ctx.input.readAllBytes()), "")
        }
        for (argument in arguments) {
            val file = ctx.shell.resolvePath(Paths.get(argument)).toFile()
            val text = file.readText()
            printWCForText(ctx, text, argument)
        }
    }

    private fun printWCForText(ctx: CommandContext, text: String, argument: String) {
        ctx.output.writeln("${text.lines().size} ${text.trim().split("\\s+".toRegex()).size} ${text.length} $argument")
    }
}