package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.AbstractCommand

class CommandSetEnviron : AbstractCommand() {
    override fun match(cmd: List<String>) = cmd.size == 1 && cmd[0].contains('=')

    override fun execute(command: String, arguments: List<String>, ctx: CommandContext): Int {
        val parts = command.split('=')
        ctx.shell.environment[parts[0]] = parts[1]
        return 0
    }
}