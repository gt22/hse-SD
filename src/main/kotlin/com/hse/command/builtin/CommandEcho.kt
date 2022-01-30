package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand

class CommandEcho : SimpleCommand("echo") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        ctx.writer.println(arguments.joinToString(separator = " "))
        return 0
    }
}