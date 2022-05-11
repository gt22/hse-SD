package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand

class CommandPWD : SimpleCommand("pwd") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        ctx.writer.println(ctx.shell.workingDirectory.toString())
        return 0
    }
}