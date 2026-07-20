package com.juliohaga.kelp.core.platform

import com.juliohaga.kelp.api.Sender

/** Abstraction over the underlying platform's sender types (Bukkit, Velocity, etc).
 *  Implemented once per platform module, e.g. PaperPlatform in kelp-paper. */
interface KelpPlatform {

    /**
     * Wraps a raw platform-specific sender (e.g. org.bukkit.command.CommandSender)
     * into a Sender — or a PlayerSender, when the raw sender represents a player.
     * This is what lets kelp-core resolve requirements like PlayerOnly without
     * ever depending on Bukkit.
     */
    fun wrapSender(rawSender: Any): Sender

    fun hasPermission(rawSender: Any, permission: String): Boolean
}