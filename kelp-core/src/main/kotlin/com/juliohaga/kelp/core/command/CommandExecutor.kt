package com.juliohaga.kelp.core.command

import com.juliohaga.kelp.api.command.CommandContext
import com.juliohaga.kelp.api.command.KelpCommand
import com.juliohaga.kelp.api.requirement.RequirementResult
import com.juliohaga.kelp.core.di.KelpContainer
import com.juliohaga.kelp.core.platform.KelpPlatform
import com.juliohaga.kelp.core.requirement.RequirementRegistryImpl

class CommandExecutor(
    private val platform: KelpPlatform,
    private val container: KelpContainer,
    private val requirementRegistry: RequirementRegistryImpl
) {

    fun instantiate(owner: kotlin.reflect.KClass<*>): Any = container.instantiate(owner)

    fun execute(
        command: ParsedCommand,
        subCommandPath: String,
        rawSender: Any,
        args: Array<String>
    ): ExecutionResult {
        val sub = command.subCommands[subCommandPath]
            ?: return ExecutionResult.SubCommandNotFound

        if (sub.permission.isNotBlank() && !platform.hasPermission(rawSender, sub.permission)) {
            return ExecutionResult.NoPermission
        }

        val sender = platform.wrapSender(rawSender)
        val fullPath = if (subCommandPath.isBlank()) command.name else "${command.name}.$subCommandPath"
        val context = CommandContext(sender, args, fullPath)

        val requirementResult = requirementRegistry.resolve(sub.requirements, context)
        if (requirementResult is RequirementResult.Denied) {
            return ExecutionResult.RequirementDenied(requirementResult.message)
        }

        return try {
            // a new instance is created per execution to avoid shared mutable state (context) across calls
            val instance = container.instantiate(sub.owner) as KelpCommand
            instance.attachContext(context)
            sub.function.call(instance)
            ExecutionResult.Success
        } catch (e: Exception) {
            ExecutionResult.ExecutionError(e)
        }
    }

    /**
     * Resolves tab-complete suggestions for a given subcommand path. Mirrors
     * execute()'s sender-wrapping / context-attaching, but skips requirement
     * checks (only permission is enforced) and never throws — a broken
     * @TabComplete function should just yield no suggestions.
     */
    fun suggest(
        command: ParsedCommand,
        subCommandPath: String,
        rawSender: Any,
        args: Array<String>
    ): List<String> {
        val tab = command.tabCompletes[subCommandPath] ?: return emptyList()

        val sub = command.subCommands[subCommandPath]
        if (sub != null && sub.permission.isNotBlank() && !platform.hasPermission(rawSender, sub.permission)) {
            return emptyList()
        }

        val sender = platform.wrapSender(rawSender)
        val fullPath = if (subCommandPath.isBlank()) command.name else "${command.name}.$subCommandPath"
        val context = CommandContext(sender, args, fullPath)

        return try {
            val instance = container.instantiate(tab.owner) as KelpCommand
            instance.attachContext(context)
            @Suppress("UNCHECKED_CAST")
            (tab.function.call(instance) as? List<String>) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}