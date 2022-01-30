package com.hse

import com.hse.command.PreparedCommand

class CommandPipeline(private val commands: List<PreparedCommand>) {
    fun execute(ctx: CommandContext): Int {
        for (command in commands) {
            command.execute(ctx)
        }
        return 0
    }
}
