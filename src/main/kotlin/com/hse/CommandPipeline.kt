package com.hse

import com.hse.command.PreparedCommand
import java.io.PipedInputStream
import java.io.PipedOutputStream

class CommandPipeline(val commands: List<PreparedCommand>) {
    fun execute(ctx: CommandContext): Int {
        if (commands.isEmpty()) return 0
        if (commands.size == 1) {
            return commands[0].execute(ctx)
        }

        val initialOutputStream = PipedOutputStream()
        var nextInputStream = PipedInputStream()
        initialOutputStream.connect(nextInputStream)

        commands[0].execute(CommandContext(ctx.shell, ctx.input, initialOutputStream))
        val middle = commands.drop(1).dropLast(1)

        for (command in middle) {
            val inputStream = nextInputStream
            nextInputStream = PipedInputStream()
            val outputStream = PipedOutputStream()
            outputStream.connect(nextInputStream)

            val curCtx = CommandContext(ctx.shell, inputStream, outputStream)
            command.execute(curCtx)
        }
        val curCtx = CommandContext(ctx.shell, nextInputStream, ctx.output)
        return commands.last().execute(curCtx)
    }
}