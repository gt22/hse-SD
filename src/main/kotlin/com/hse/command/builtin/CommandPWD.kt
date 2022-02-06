package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand

class CommandPWD : SimpleCommand("pwd") {
    // prints absolute path of the working directory
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        ctx.writer.println(ctx.shell.workingDirectoryAbsolutePath.toString())
        return 0
    }
}