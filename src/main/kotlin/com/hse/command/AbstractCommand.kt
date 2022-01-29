package com.hse.command

import com.hse.CommandContext

abstract class AbstractCommand {
    abstract fun match(cmd: List<String>): Boolean

    abstract fun execute(arguments: List<String>, ctx: CommandContext)
}
