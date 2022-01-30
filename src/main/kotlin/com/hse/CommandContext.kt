package com.hse

import java.io.InputStream
import java.io.OutputStream

class CommandContext(val shell: Shell, val input: InputStream, val output: OutputStream)
