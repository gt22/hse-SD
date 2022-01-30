package com.hse.command

import com.hse.CommandContext
import java.io.IOException

class ExternalCommand : AbstractCommand() {
    override fun match(cmd: List<String>) = true

    override fun execute(command: String, arguments: List<String>, ctx: CommandContext): Int {
        val cmd = ArrayList<String>(arguments.size + 1)
        cmd[0] = command
        cmd.addAll(arguments)
        val process =
            ProcessBuilder(cmd).apply {
                directory(ctx.shell.workingDirectoryAbsolutePath.toFile())
                redirectError(ProcessBuilder.Redirect.INHERIT)
                environment().putAll(ctx.shell.environment)
            }.start()
        try {
            ctx.input.transferTo(process.outputStream)
        } catch (e: IOException) {
            // process ended
        }
        process.waitFor()
        process.inputStream.use {
            it.transferTo(ctx.output)
        }
        return process.exitValue()
    }
}
