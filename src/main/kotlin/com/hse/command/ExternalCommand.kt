package com.hse.command

import com.hse.CommandContext
import java.io.IOException

/**
 * Внешняя команда - запускает команду как системную
 */
class ExternalCommand : ICommand {
    override fun match(cmd: List<String>) = true

    override fun execute(command: String, arguments: List<String>, ctx: CommandContext): Int {
        val cmd = ArrayList<String>(arguments.size + 1)
        cmd.add(command)
        cmd.addAll(arguments)
        try {
            val process =
                ProcessBuilder(cmd).apply {
                    directory(ctx.shell.workingDirectory.toFile())
                    redirectError(ProcessBuilder.Redirect.INHERIT)
                    if (ctx.input == System.`in`) {
                        redirectInput(ProcessBuilder.Redirect.INHERIT)
                    }
                    environment().putAll(ctx.shell.environment)
                }.start()
            if(ctx.input != System.`in`) {
                try {
                    ctx.input.transferTo(process.outputStream)
                    process.outputStream.close()
                } catch (e: IOException) {
                    println("IOE")
                    //no-op
                }
            }
            process.waitFor()
            process.outputStream.close()
            process.inputStream.use {
                it.transferTo(ctx.output)
            }
            return process.exitValue()
        } catch (e: IOException) {
            return 120
        }
    }
}
