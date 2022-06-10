package com.hse.command.builtin

import java.io.InputStream

internal class CommandGrepTest {
    @org.junit.jupiter.api.Test
    fun execute() {
        testCompareWithExternal(listOf("1")) { getStream("/testFile.txt") }
    }

    @org.junit.jupiter.api.Test
    fun `test afterContext flag`() {
        testCompareWithExternal(listOf("1", "-A", "3")) { getStream("/test") }
    }

    @org.junit.jupiter.api.Test
    fun `test -w flag`() {
        testCompareWithExternal(listOf("23", "-w")) { getStream("/testW") }
    }
    @org.junit.jupiter.api.Test
    fun `test without -w flag`() {
        testCompareWithExternal(listOf("23")) { getStream("/testW") }
    }


    private fun testCompareWithExternal(arguments: List<String>, getStream: () -> InputStream) {
        testCompareWithExternal("grep", arguments, getStream)
    }

    private fun getStream(file: String) = javaClass.getResourceAsStream(file)!!
}