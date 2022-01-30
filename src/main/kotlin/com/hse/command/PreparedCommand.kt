package com.hse.command

import com.hse.CommandContext

class PreparedCommand(private val command: AbstractCommand, private val commandString: String, private val arguments: List<String>) {
    fun execute(ctx: CommandContext) = command.execute(commandString, arguments, ctx)
}
