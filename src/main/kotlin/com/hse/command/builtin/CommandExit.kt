package com.hse.command.builtin

import com.hse.CommandContext
import com.hse.command.SimpleCommand

class CommandExit : SimpleCommand("exit") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        throw ExitException()
    }
}

class ExitException : java.lang.Exception("Exit command")