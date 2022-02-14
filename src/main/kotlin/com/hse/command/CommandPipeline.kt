package com.hse.command

import com.hse.CommandContext
import com.hse.command.builtin.ExitException
import java.io.PipedInputStream
import java.io.PipedOutputStream

/**
 * Набор команд для последовательного исполнения, где вывод одной команды является вводом следующей.
 */
class CommandPipeline(private val commands: List<PreparedCommand>) {

    /**
     * Последовательно запускает команды в пайплайне, и обеспечивает корректную передачу ввода/вывода между ними
     */
    fun execute(ctx: CommandContext): Int {
        if(commands.isEmpty()) return 0
        var curIn = ctx.input
        var curOut = PipedOutputStream()
        for (cmd in commands.dropLast(1)) {
            val nextIn = PipedInputStream(curOut)
            curOut.use {
                try {
                    cmd.execute(CommandContext(ctx.shell, curIn, curOut))
                } catch (e: ExitException) {
                    throw e
                } catch (e: Exception) {
                    //no-op
                }
            }
            curIn = nextIn
            curOut = PipedOutputStream()
        }
        return commands.last().execute(CommandContext(ctx.shell, curIn, ctx.output))
    }

}