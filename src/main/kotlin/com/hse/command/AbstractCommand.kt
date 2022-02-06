package com.hse.command

import com.hse.CommandContext

abstract class AbstractCommand {
    // returns true iff this command matches given list of tokens
    abstract fun match(cmd: List<String>): Boolean

    abstract fun execute(command: String, arguments: List<String>, ctx: CommandContext): Int
}
