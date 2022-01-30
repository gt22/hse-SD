package com.hse

import java.io.OutputStream

fun OutputStream.write(s: String) {
    write(s.toByteArray())
}

fun OutputStream.writeln(s: String) {
    write(s.toByteArray())
    write(System.lineSeparator())
}

fun OutputStream.writeln(s: ByteArray) {
    write(s)
    write(System.lineSeparator())
}