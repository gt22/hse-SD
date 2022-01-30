package com.hse

import com.hse.command.AbstractCommand
import com.hse.command.PreparedCommand

class Parser(private val commandsList: List<AbstractCommand>) {
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

        val command = commandsList.first { it.match(tokens) }
        return PreparedCommand(command, tokens[0], tokens.drop(1))
    }
}
