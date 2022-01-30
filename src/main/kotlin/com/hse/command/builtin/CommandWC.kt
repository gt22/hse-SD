package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.nio.file.Files
import java.nio.file.Paths

class CommandWC : SimpleCommand("wc") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        if (arguments.isEmpty()) {
            printWCForText(ctx, ctx.reader.readText(), "")
        }
        for (filename in arguments) {
            val file = ctx.shell.resolvePath(Paths.get(filename))
            printWCForText(ctx, Files.readString(file), filename)
        }
    }

    private fun printWCForText(ctx: CommandContext, text: String, filename: String) {
        ctx.writer.println("${text.lines().size} ${text.trim().split("\\s+".toRegex()).size} ${text.length} $filename")
    }
}