package com.hse.command

import com.hse.Parser
import com.hse.Shell
import com.hse.command.builtin.CommandCat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserTest {

    private fun String.testParser(
        commands: List<ICommand>,
        environment: Map<String, String>,
        expected: CommandPipeline?
    ) {
        val shell = Shell(commands)
        shell.environment.putAll(environment)
        val parser = Parser(shell, commands)
        assertEquals(expected, parser.parseWithSubstitution(this))
    }

    @Test
    fun testSimple() = CommandCat().let { cat ->
        "cat".testParser(
            listOf(cat), emptyMap(), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", emptyList()))
            )
        )
    }

    @Test
    fun testSimpleArguments() = CommandCat().let { cat ->
        "cat file".testParser(
            listOf(cat), emptyMap(), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    @Test
    fun testSingleQuotes() = CommandCat().let { cat ->
        "cat 'file'".testParser(
            listOf(cat), emptyMap(), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    @Test
    fun testDoubleQuotes() = CommandCat().let { cat ->
        "cat \"file\"".testParser(
            listOf(cat), emptyMap(), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    /**
     * Слова вида `'fi'le` считаются одним аргументом `file`
     */
    @Test
    fun testTokenConcatenation() = CommandCat().let { cat ->
        for (a in listOf("", "'", "\"")) {
            for (b in listOf("", "'", "\"")) {
                "cat ${a}fi${a}${b}le${b}".testParser(
                    listOf(cat), emptyMap(), CommandPipeline(
                        listOf(PreparedCommand(cat, "cat", listOf("file")))
                    )
                )
            }
        }
    }

    @Test
    fun testSimpleSubstitution() = CommandCat().let { cat ->
        "cat \$arg".testParser(
            listOf(cat), mapOf("arg" to "file"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    @Test
    fun testSingleQuoteNoSubstitution() = CommandCat().let { cat ->
        "cat '\$arg'".testParser(
            listOf(cat), mapOf("arg" to "file"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("\$arg")))
            )
        )
    }

    @Test
    fun testDoubleQuoteSubstitution() = CommandCat().let { cat ->
        "cat \"\$arg\"".testParser(
            listOf(cat), mapOf("arg" to "file"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    @Test
    fun testDoubleSubstitution() = CommandCat().let { cat ->
        "cat \$arg1\$arg2".testParser(
            listOf(cat), mapOf("arg1" to "fi", "arg2" to "le"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("file")))
            )
        )
    }

    @Test
    fun testTwoSubstitutions() = CommandCat().let { cat ->
        "cat \$arg1 \$arg2".testParser(
            listOf(cat), mapOf("arg1" to "fi", "arg2" to "le"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("fi", "le")))
            )
        )
    }

    @Test
    fun testComplexConcat() = CommandCat().let { cat ->
        "cat \"\$arg1\"'\$arg2'".testParser(
            listOf(cat), mapOf("arg1" to "fi", "arg2" to "le"), CommandPipeline(
                listOf(PreparedCommand(cat, "cat", listOf("fi\$arg2")))
            )
        )
    }

}