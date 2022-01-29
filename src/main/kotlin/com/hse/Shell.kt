package com.hse

class Shell(val commandContext: CommandContext) {
    //    no variables in 1st iteration
    //    val environment: Map<String, String> = mapOf()
    private val parser = Parser(commandContext)

    fun execute(line: String): Int {
        val commandPipeline = parser.parseWithSubstitution(line)
        commandPipeline.execute(commandContext)

        return 0
    }
}