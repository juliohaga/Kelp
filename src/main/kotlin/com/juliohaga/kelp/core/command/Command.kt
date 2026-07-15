package com.juliohaga.kelp.core.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

interface Command : CommandExecutor, TabCompleter {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return execute(sender, args)
    }

    fun getCommand(): String
    fun getUsage(): String
    fun getPermission(): String?
    fun help(sender: CommandSender)
    fun getTabCompleter(): TabCompleter? = null

    fun execute(
        sender: CommandSender,
        args: Array<out String>
    ): Boolean



}