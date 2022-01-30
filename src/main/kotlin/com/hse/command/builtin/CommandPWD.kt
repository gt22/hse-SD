package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand
import com.hse.writeln

class CommandPWD : SimpleCommand("pwd") {
    override fun execute(arguments: List<String>, ctx: CommandContext) {
        ctx.output.writeln(ctx.shell.workingDirectoryAbsolutePath.toString())
    }
}