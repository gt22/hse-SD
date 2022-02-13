package com.hse

import com.hse.command.AbstractCommand
import com.hse.command.ExternalCommand
import com.hse.command.PreparedCommand

class Parser(private val commandsList: List<AbstractCommand>) {
    fun parseWithSubstitution(line: String, environment: MutableMap<String, String>): CommandPipeline {
        val pipeline = mutableListOf<PreparedCommand>()
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
                environment[variableName.toString()] ?: ""
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
                '|' -> {
                    addCommandToPipeline(newWord, tokens, pipeline)
                    currentPosition++
                }
                else -> {
                    newWord.append(line[currentPosition++])
                }
            }
        }
        addCommandToPipeline(newWord, tokens, pipeline)
        return CommandPipeline(pipeline)
    }

    private fun addCommandToPipeline(
        newWord: StringBuilder,
        tokens: MutableList<String>,
        pipeline: MutableList<PreparedCommand>
    ) {
        if (newWord.isNotEmpty()) {
            tokens += newWord.toString()
        }
        if (tokens.isEmpty()) return
        newWord.clear()
        val command = commandsList.firstOrNull { it.match(tokens) } ?: ExternalCommand()
        val prepared = PreparedCommand(command, tokens[0], tokens.drop(1))
        tokens.clear()
        pipeline.add(prepared)
    }
}
