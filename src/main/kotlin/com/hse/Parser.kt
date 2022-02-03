package com.hse

import com.hse.command.AbstractCommand
import com.hse.command.ExternalCommand
import com.hse.command.PreparedCommand

class Parser(private val commandsList: List<AbstractCommand>) {
    fun parseWithSubstitution(line: String, environment: MutableMap<String, String>): PreparedCommand? {
        val tokens = mutableListOf<String>()

        val newWord = StringBuilder()
        var currentPosition = 0

        val parseVariable = fun() {
            currentPosition++
            val variableName = StringBuilder()
            while (currentPosition < line.length && (line[currentPosition].isLetterOrDigit() || line[currentPosition] == '_')) {
                variableName.append(line[currentPosition])
                currentPosition++
            }
            newWord.append(
                if (variableName.isNotEmpty())
                    environment.get(variableName.toString()) ?: ""
                else '$'
            )
        }

        val parseQuote = fun() {
            val openedQuote = line[currentPosition++]
            while (currentPosition < line.length && line[currentPosition] != openedQuote) {
                if (openedQuote == '"' && line[currentPosition] == '$') {
                    parseVariable()
                } else {
                    newWord.append(line[currentPosition++])
                }
            }
            if (currentPosition < line.length && line[currentPosition] == openedQuote) {
                currentPosition++
            } else if (currentPosition == line.length) {
                throw IllegalArgumentException("There is an unmatched quote")
            }
        }

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
                    parseQuote()
                }
                '$' -> {
                    parseVariable()
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
