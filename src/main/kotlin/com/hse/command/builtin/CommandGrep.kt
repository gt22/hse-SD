package com.hse.command.builtin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.lang.Integer.max
import java.nio.file.Files
import java.nio.file.Paths

class CommandGrep : SimpleCommand("grep") {
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        val argumentsParser = ArgumentsParser()
        argumentsParser.main(arguments)
        val (pattern, filename, ignoreCase, wordRegexp, afterContext) = argumentsParser.grepArguments()

        val toFindRegex = pattern.toRegex(
            if (ignoreCase) {
                setOf(RegexOption.IGNORE_CASE)
            } else emptySet()
        )

        val lines = if (filename != null) {
            val file = ctx.shell.resolvePath(Paths.get(filename))
            Files.readAllLines(file)
        } else {
            ctx.reader.readLines()
        }
        var maxtoPrint = -1
        lines.forEachIndexed { index, line ->
            toFindRegex.find(line)?.let {
                if (!wordRegexp || (wordRegexp && notConstituent(line, it))) {
                    maxtoPrint = max(maxtoPrint, index + afterContext)
                }
            }
            if (index <= maxtoPrint) {
                ctx.writer.println(lines[index])
            }
        }

        return 0
    }

    private fun notConstituent(input: String, match: MatchResult): Boolean {
        return input.getOrNull(match.range.first - 1).notConstituent() &&
                input.getOrNull(match.range.last + 1).notConstituent()
    }

    private fun Char?.notConstituent() = this == null || !(isLetter() || isDigit() || this == '_')
}

private data class ParsedArguments(
    val pattern: String,
    val filename: String?,
    val ignoreCase: Boolean,
    val wordRegexp: Boolean,
    val afterContext: Int
)

private class ArgumentsParser : CliktCommand() {
    private val ignoreCase by option("-i").flag()
    private val wordRegexp by option("-w").flag()
    private val afterContext by option("-A").int().default(0)
    private val pattern by argument()
    private val filename by argument().optional()

    fun grepArguments() = ParsedArguments(pattern, filename, ignoreCase, wordRegexp, afterContext)

    override fun run() = Unit
}

