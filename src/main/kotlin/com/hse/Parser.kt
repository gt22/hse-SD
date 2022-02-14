package com.hse

import com.hse.command.CommandPipeline
import com.hse.command.ICommand
import com.hse.command.ExternalCommand
import com.hse.command.PreparedCommand

/**
 * Парсер отвечающий за разбор строк на команды
 * @param shell - Шелл для доспута к переменным среды
 * @param commandsList - список встроенных команд, при разборе выбирается первая подходящая, если таких нет - [ExternalCommand]
 */
class Parser(
    private val shell: Shell,
    private val commandsList: List<ICommand>
    ) {

    /**
     * Разбирает строку на набор команд, с учётом подстановок
     * @return `null` если не получилось ни одного токена, команду с аргументами иначе
     */
    fun parseWithSubstitution(line: String): CommandPipeline? {
        val commands = mutableListOf<PreparedCommand>()
        val tokens = mutableListOf<String>()

        val newWord = StringBuilder()
        val tokenSegments = mutableListOf<String>()
        // null => no quote yet
        var openedQuote: Char?
        var currentPosition = 0
        while (currentPosition <= line.length) {
            when (val c = if(currentPosition < line.length) line[currentPosition] else ' ') {
                ' ', '|' -> {
                    if (newWord.isNotEmpty()) {
                        tokenSegments += attemptSubstitution(newWord.toString())
                        newWord.clear()
                    }
                    if(tokenSegments.isNotEmpty()) {
                        tokens += tokenSegments.joinToString("")
                        tokenSegments.clear()
                    }
                    if(c == '|' && currentPosition == line.length) {
                        throw IllegalArgumentException("Tail pipe is not allowed")
                    }
                    if(c == '|' || currentPosition == line.length) {
                        if(tokens.isEmpty()) {
                            throw IllegalArgumentException("Empty command")
                        }
                        val command = commandsList.firstOrNull { it.match(tokens) } ?: ExternalCommand()
                        commands += PreparedCommand(command, tokens[0], tokens.drop(1))
                        tokens.clear()
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
                        tokenSegments += if(openedQuote == '\'') {
                            newWord.toString()
                        } else {
                            attemptSubstitution(newWord.toString())
                        }
                        newWord.clear()
                    } else if (currentPosition == line.length) {
                        throw IllegalArgumentException("There is an unmatched quote")
                    }
                }
                else -> {
                    newWord.append(line[currentPosition++])
                }
            }
        }

        if (commands.isEmpty()) return null

        return CommandPipeline(commands)
    }

    private fun attemptSubstitution(s: String): String {
        var res = s
        var start = 0
        while(true) {
            val m = "\\$([a-zA-Z_][a-zA-Z_\\d]*)".toRegex().find(res, start) ?: break
            val env = m.groupValues[1]
            val value = shell.environment.getOrDefault(env, System.getenv(env) ?: "")
            res = res.replaceRange(m.range, value)
            start = m.range.first + value.length
        }
        return res
    }
}
