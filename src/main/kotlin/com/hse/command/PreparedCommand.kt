package com.hse.command

import com.hse.CommandContext

class PreparedCommand(val command: AbstractCommand, private val arguments: List<String>) {
    fun execute(ctx: CommandContext): Int {
        return command.execute(arguments, ctx)
    }
}
