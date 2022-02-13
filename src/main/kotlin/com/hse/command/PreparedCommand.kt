package com.hse.command

import com.hse.CommandContext

/**
 * Задаёт команду с конкретными аргументами. Результат разбора [Parser][com.hse.Parser]
 */
class PreparedCommand(private val command: ICommand, private val commandString: String, private val arguments: List<String>) {
    fun execute(ctx: CommandContext) = command.execute(commandString, arguments, ctx)
}
