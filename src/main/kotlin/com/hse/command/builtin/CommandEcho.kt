package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import com.hse.writeln

class CommandEcho : SimpleCommand("echo") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        ctx.output.writeln(arguments.joinToString(separator = " ").toByteArray())
    }
}