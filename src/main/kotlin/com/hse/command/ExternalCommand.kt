package com.hse.command

import com.hse.CommandContext

class ExternalCommand(private val line: String) : AbstractCommand() {
    override fun match(cmd: List<String>): Boolean {
        return true
    }

    override fun execute(arguments: List<String>, ctx: CommandContext) {
        val process = Runtime.getRuntime().exec(line)
        process.waitFor()
        process.inputStream.use {
            ctx.output.write(it.readAllBytes())
        }
    }
}
