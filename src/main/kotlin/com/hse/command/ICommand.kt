package com.hse.command

import com.hse.CommandContext

/**
 * Интерфейс, которому удовлетворяют все команды
 */
interface ICommand {

    /**
     * Проверяет, задаёт-ли набор токенов данную команду, используется для разбора в [Parser][com.hse.Parser]
     */
    fun match(cmd: List<String>): Boolean

    /**
     * Исполняет команду
     * @return код возврата команды
     */
    fun execute(command: String, arguments: List<String>, ctx: CommandContext): Int
}
