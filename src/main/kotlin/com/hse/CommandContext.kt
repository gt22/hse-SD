package com.hse

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter

class CommandContext(val shell: Shell, val input: InputStream, val output: OutputStream) {

    val reader by lazy { input.bufferedReader() }
    val writer by lazy { PrintWriter(output, true) }

}
