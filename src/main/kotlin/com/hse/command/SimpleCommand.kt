package com.hse.command

abstract class SimpleCommand(private val name: String) : AbstractCommand() {
    override fun match(cmd: List<String>): Boolean {
        return cmd.firstOrNull() == name
    }
}