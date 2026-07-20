package com.juliohaga.kelp.paper.command

import com.juliohaga.kelp.core.command.CommandExecutor
import com.juliohaga.kelp.core.command.ExecutionResult
import com.juliohaga.kelp.core.command.ParsedCommand
import com.mojang.brigadier.Command as BrigadierCommand
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

class PaperCommandBridge(
    private val executor: CommandExecutor
) {

    fun build(
        command: ParsedCommand
    ): LiteralCommandNode<CommandSourceStack> {

        val root = Commands.literal(command.name)

        command.subCommands[""]?.let {
            root.executes { ctx ->
                handle(command, "", ctx)
            }
        }

        root.then(
            Commands.argument("args", StringArgumentType.greedyString())
                .suggests { ctx, builder -> suggest(command, "", ctx, builder) }
                .executes { ctx ->
                    handle(command, "", ctx)
                }
        )

        command.subCommands
            .values
            .filter {
                it.path.isNotBlank()
            }
            .forEach { sub ->

                root.then(
                    Commands.literal(sub.path)
                        .executes { ctx ->
                            handle(command, sub.path, ctx)
                        }
                        .then(
                            Commands.argument("args", StringArgumentType.greedyString())
                                .suggests { ctx, builder -> suggest(command, sub.path, ctx, builder) }
                                .executes { ctx ->
                                    handle(command, sub.path, ctx)
                                }
                        )
                )
            }

        return root.build()
    }

    /** Drops the first whitespace-delimited token of [input] and returns
     *  everything after it (trailing whitespace preserved, so an
     *  in-progress trailing word — or its absence — is kept intact). */
    private fun dropFirstToken(input: String): String {
        val trimmedStart = input.trimStart()
        val spaceIndex = trimmedStart.indexOf(' ')
        return if (spaceIndex == -1) "" else trimmedStart.substring(spaceIndex + 1)
    }

    /**
     * Strips both the command name and, when present, the subcommand
     * literal itself from the raw input — so every @SubCommand /
     * @TabComplete function always sees args[0] as its FIRST real
     * parameter. Token-based (not string-prefix-based) on purpose: Brigadier
     * feeds command execution input WITHOUT a leading '/', but tab-complete
     * suggestion input WITH a leading '/' — a naive removePrefix(command.name)
     * silently fails to strip anything in the suggestion case, since the
     * string starts with "/world", not "world". Dropping tokens by position
     * instead of by literal-prefix match works correctly in both cases.
     */
    private fun stripPrefix(input: String, subCommandPath: String): String {

        var remaining = input.trimStart()

        if (remaining.startsWith("/")) {
            remaining = remaining.substring(1)
        }

        remaining = dropFirstToken(remaining) // drop command name/alias

        if (subCommandPath.isNotBlank()) {
            remaining = dropFirstToken(remaining) // drop subcommand literal
        }

        return remaining
    }

    private fun suggest(
        command: ParsedCommand,
        subCommandPath: String,
        ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>,
        builder: com.mojang.brigadier.suggestion.SuggestionsBuilder
    ): java.util.concurrent.CompletableFuture<com.mojang.brigadier.suggestion.Suggestions> {

        val sender = ctx.source.sender

        // NOTE: no trim() on the end here on purpose. Trimming drops a
        // trailing space, which makes it impossible to tell "still typing
        // the Nth word" apart from "finished the Nth word, about to type
        // the (N+1)th" — both would collapse to the same args.size and
        // break position-based tab completes. split(" ") keeps a trailing
        // "" entry for the in-progress argument instead.
        val remainingInput = stripPrefix(ctx.input, subCommandPath)
        val args = remainingInput.split(" ").toTypedArray()

        val suggestions = executor.suggest(command, subCommandPath, sender, args)

        // only the word currently being typed should be replaced, not the
        // whole greedy "args" capture — otherwise Brigadier tries to match
        // full multi-word input against single-word candidates
        val remaining = builder.remaining
        val lastWord = remaining.substringAfterLast(' ')
        val offsetBuilder = builder.createOffset(builder.input.length - lastWord.length)

        suggestions
            .filter { it.startsWith(lastWord, ignoreCase = true) }
            .forEach(offsetBuilder::suggest)

        return offsetBuilder.buildFuture()
    }

    private fun handle(
        command: ParsedCommand,
        subCommandPath: String,
        ctx: com.mojang.brigadier.context.CommandContext<CommandSourceStack>
    ): Int {

        val sender =
            ctx.source.sender

        val args =
            stripPrefix(ctx.input, subCommandPath)
                .trim()
                .split(" ")
                .filter {
                    it.isNotBlank()
                }
                .toTypedArray()

        return when(
            val result =
                executor.execute(
                    command,
                    subCommandPath,
                    sender,
                    args
                )
        ) {

            ExecutionResult.Success ->
                BrigadierCommand.SINGLE_SUCCESS

            ExecutionResult.NoPermission -> {

                sender.sendPlainMessage(
                    "You don't have permission."
                )

                BrigadierCommand.SINGLE_SUCCESS
            }

            ExecutionResult.SubCommandNotFound -> {

                sender.sendPlainMessage(
                    "Subcommand not found."
                )

                BrigadierCommand.SINGLE_SUCCESS
            }

            is ExecutionResult.RequirementDenied -> {

                sender.sendPlainMessage(
                    result.message
                )

                BrigadierCommand.SINGLE_SUCCESS
            }

            is ExecutionResult.ExecutionError -> {

                result.cause.printStackTrace()

                BrigadierCommand.SINGLE_SUCCESS
            }
        }
    }
}