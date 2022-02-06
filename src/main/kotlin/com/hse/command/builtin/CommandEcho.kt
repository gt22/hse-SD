package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand

class CommandEcho : SimpleCommand("echo") {
    // prints given arguments separated by space
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        ctx.writer.println(arguments.joinToString(separator = " "))
        return 0
    }
}