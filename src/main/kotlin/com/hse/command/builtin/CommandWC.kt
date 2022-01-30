package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Files
import java.nio.file.Paths

class CommandWC : SimpleCommand("wc") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        if (arguments.isEmpty()) {
            printWCForText(ctx, ctx.reader.readText(), null)
        }
        for (filename in arguments) {
            val file = ctx.shell.resolvePath(Paths.get(filename))
            printWCForText(ctx, Files.readString(file), filename)
        }
        return 0
    }

    private fun printWCForText(ctx: CommandContext, text: String, filename: String?) {
        ctx.writer.print("${text.lines().size} ${text.trim().split("\\s+".toRegex()).size} ${text.length}")
        if(filename != null) {
            ctx.writer.print(" ")
            ctx.writer.print(filename)
        }
        ctx.writer.println()
    }
}