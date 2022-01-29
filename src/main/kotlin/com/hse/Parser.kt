package com.hse

import com.hse.command.ExternalCommand
import com.hse.command.PreparedCommand
import com.hse.command.SimpleCommand

class Parser(private val simpleCommands: List<SimpleCommand>) {
    fun parseWithSubstitution(line: String): CommandPipeline {
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

        if (tokens.isEmpty()) return CommandPipeline(listOf())

        val command = simpleCommands.firstOrNull { it.match(tokens) } ?: ExternalCommand(line)
        return CommandPipeline(listOf(PreparedCommand(command, tokens.drop(1))))
    }
}
