package com.juliohaga.kelp.core.command

import com.juliohaga.kelp.api.command.Command
import com.juliohaga.kelp.api.command.KelpCommand
import com.juliohaga.kelp.api.command.SubCommand
import com.juliohaga.kelp.api.command.TabComplete
import com.juliohaga.kelp.core.requirement.RequirementCollector
import com.juliohaga.kelp.core.scanner.ClassScanner
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

class CommandRegistry(
    private val scanner: ClassScanner
) {

    fun scan(): List<ParsedCommand> {
        return scanner.findAnnotatedWith(Command::class)
            .filter { KelpCommand::class.java.isAssignableFrom(it.java) }
            .map(::parse)
    }

    private fun parse(owner: KClass<*>): ParsedCommand {

        val commandAnnotation = owner.findAnnotation<Command>()
            ?: error("${owner.simpleName} is annotated with @Command but annotation could not be read")

        val subCommands = owner.memberFunctions
            .mapNotNull { function ->
                function.findAnnotation<SubCommand>()?.let { function to it }
            }
            .associate { (function, sub) ->

                val permission = sub.permission.ifBlank { commandAnnotation.permission }

                // merges requirements declared on the class (@Command) with the
                // ones declared on this specific subcommand function
                val requirements = RequirementCollector.collect(owner, function)

                sub.path to ParsedSubCommand(
                    path = sub.path,
                    permission = permission,
                    requirements = requirements,
                    owner = owner,
                    function = function
                )
            }

        val tabCompletes = owner.memberFunctions
            .mapNotNull { function ->
                function.findAnnotation<TabComplete>()?.let { function to it }
            }
            .associate { (function, annotation) ->
                annotation.path to ParsedTabComplete(
                    path = annotation.path,
                    owner = owner,
                    function = function
                )
            }

        return ParsedCommand(
            name = commandAnnotation.name,
            permission = commandAnnotation.permission,
            aliases = commandAnnotation.aliases.toList(),
            subCommands = subCommands,
            tabCompletes = tabCompletes
        )
    }
}