package com.juliohaga.kelp.core.command

class ParsedCommand(
    val name: String,
    val permission: String,
    val aliases: List<String>,
    val subCommands: Map<String, ParsedSubCommand>,
    val tabCompletes: Map<String, ParsedTabComplete>
)