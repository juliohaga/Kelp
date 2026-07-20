package com.juliohaga.kelp.paper.command

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

/** The KelpCommand plugin developers actually extend. Unwraps the platform-
 *  independent Sender from kelp-api back into concrete Bukkit types. */
abstract class KelpCommand : com.juliohaga.kelp.api.command.KelpCommand() {

    protected val bukkitSender: CommandSender
        get() = (sender as PaperSender).bukkit

    /**
     * Non-null by design: only safe to call when the subcommand is annotated
     * with @PlayerOnly (or another requirement that guarantees a player sender).
     * Throws with a clear message otherwise, instead of a silent null or a
     * confusing ClassCastException.
     */
    protected val player: Player
        get() = (sender as? PaperPlayerSender)?.player
            ?: error("player was accessed but the sender is not a Player — annotate this subcommand with @PlayerOnly")

    protected val playerOrNull: Player?
        get() = (sender as? PaperPlayerSender)?.player

    protected val console: ConsoleCommandSender?
        get() = bukkitSender as? ConsoleCommandSender
}