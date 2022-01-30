package com.hse.command

import com.hse.CommandContext
import java.io.IOException

class ExternalCommand(private val command: String) : AbstractCommand() {
    override fun match(cmd: List<String>) = true

    override fun execute(arguments: List<String>, ctx: CommandContext): Int {
        val cmd = ArrayList<String>(arguments.size + 1)
        cmd[0] = command
        cmd.addAll(arguments)
        val process =
            ProcessBuilder(cmd)
                .directory(ctx.shell.workingDirectoryAbsolutePath.toFile())
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
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
