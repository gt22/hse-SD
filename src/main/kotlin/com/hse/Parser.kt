package com.hse

import com.hse.command.ICommand
import com.hse.command.ExternalCommand
import com.hse.command.PreparedCommand

/**
 * Парсер отвечающий за разбор строк на команды
 * @param commandsList - список встроенных команд, при разборе выбирается первая подходящая, если таких нет - [ExternalCommand]
 */
class Parser(private val commandsList: List<ICommand>) {

    /**
     * Разбирает строку на набор команд, с учётом подстановок (@todo - вторая часть задания)
     * @return `null` если не получилось ни одного токена, команду с аргументами иначе
     */
    fun parseWithSubstitution(line: String): PreparedCommand? {
        val tokens = mutableListOf<String>()

        val newWord = StringBuilder()
        // null => no quote yet
        var openedQuote: Char?
        var currentPosition = 0
        while (currentPosition < line.length) {
            when (line[currentPosition]) {
                ' ' -> {
                    if (newWord.isNotEmpty()) {
                        tokens += newWord.toString()
                        newWord.clear()
                    }
                    currentPosition++
                    continue
                }
                '"', '\'' -> {
                    openedQuote = line[currentPosition++]
                    while (currentPosition < line.length && line[currentPosition] != openedQuote) {
                        newWord.append(line[currentPosition++])
                    }
                    if (currentPosition < line.length && line[currentPosition] == openedQuote) {
                        currentPosition++
                    } else if (currentPosition == line.length) {
                        throw IllegalArgumentException("There is an unmatched quote")
                    }
                }
                else -> {
                    newWord.append(line[currentPosition++])
                }
            }
        }

        if (newWord.isNotEmpty()) {
            tokens += newWord.toString()
        }

        if (tokens.isEmpty()) return null

        val command = commandsList.firstOrNull { it.match(tokens) } ?: ExternalCommand()
        return PreparedCommand(command, tokens[0], tokens.drop(1))
    }
}
