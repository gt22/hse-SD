package com.hse

import com.hse.command.builtin.*


fun main() {
    val ctx = Shell(
        listOf(
            CommandCat(),
            CommandEcho(),
            CommandExit(),
            CommandPWD(),
            CommandWC(),
            CommandGrep(),
            CommandSetEnviron(),
            CommandCD(),
            CommandLS()
        )
    )
    ctx.startShell()
}
