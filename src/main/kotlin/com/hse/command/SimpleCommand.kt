package com.hse.command

import com.hse.CommandContext

/**
 * Простая команда - команда имеющая конкретное имя
 */
abstract class SimpleCommand(private val name: String) : ICommand {
    override fun match(cmd: List<String>) = cmd.firstOrNull() == name

    override fun execute(command: String, arguments: List<String>, ctx: CommandContext) = execute(arguments, ctx)

    protected abstract fun execute(arguments: List<String>, ctx: CommandContext): Int
}