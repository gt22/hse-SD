package com.hse.command.builtin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.check
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.hse.CommandContext
import com.hse.command.SimpleCommand
import java.lang.Integer.max
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence

/**
 * grep searches for patterns in file (or input stream), prints each line that matches a pattern
 *
 * OPTIONS:
 * * `-i` — ignore case
 * * `-w`(word regexp) — Select only those lines containing matches that form  whole words
 * * `-A NUM` (after context) — Print NUM lines of trailing context after matching  line
 */
class CommandGrep : SimpleCommand("grep") {
    /**
     * arguments: pattern [file name] [flags with values]
     */
    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        val (pattern, filename, ignoreCase, wordRegexp, afterContext) = parseArgs(arguments)
        val toFindRegex = getRegex(pattern, ignoreCase)

        var maxToPrint = -1
        getLines(filename, ctx).forEachIndexed { index, line ->
            if (findByRegex(toFindRegex, line, wordRegexp)) {
                maxToPrint = max(maxToPrint, index + afterContext)
            }
            if (index <= maxToPrint) {
                ctx.writer.println(line)
            }
        }

        return 0
    }

    private fun findByRegex(toFindRegex: Regex, line: String, wordRegexp: Boolean): Boolean {
        return toFindRegex.find(line)?.let { !wordRegexp || (wordRegexp && notConstituent(line, it)) } == true
    }

    private fun getLines(filename: String?, ctx: CommandContext) = if (filename != null) {
        val file = ctx.shell.resolvePath(Paths.get(filename))
        Files.lines(file)
    } else {
        ctx.reader.lines()
    }.asSequence()

    private fun getRegex(pattern: String, ignoreCase: Boolean) = pattern.toRegex(
        if (ignoreCase) {
            setOf(RegexOption.IGNORE_CASE)
        } else emptySet()
    )

    private fun parseArgs(arguments: List<String>): ParsedArguments {
        val argumentsParser = ArgumentsParser()
        try {
            argumentsParser.parse(arguments)
        } catch (e: CliktError) {
            throw IllegalArgumentException(e.message)
        }
        return argumentsParser.grepArguments()
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
        .check("Should be a non-negative integer") { it >= 0 }
    private val pattern by argument()
    private val filename by argument().optional()

    fun grepArguments() = ParsedArguments(pattern, filename, ignoreCase, wordRegexp, afterContext)

    override fun run() = Unit
}

