package com.hse.command.builtin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CommandCatTest {
    @Test
    fun execute() {
        val result = testCommand(javaClass.getResourceAsStream("/testFile.txt")!!) { ctx ->
            CommandCat().execute("cat", emptyList(), ctx)
        }
        assertEquals("1 2${ln}3 4", result)
    }
}