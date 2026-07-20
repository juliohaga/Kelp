package com.juliohaga.kelp.paper

import com.juliohaga.kelp.api.Sender
import com.juliohaga.kelp.core.platform.KelpPlatform
import com.juliohaga.kelp.paper.command.PaperPlayerSender
import com.juliohaga.kelp.paper.command.PaperSender
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/** Bukkit/Paper implementation of KelpPlatform — the only place in kelp-paper
 *  where "is this a player" logic lives. */
class PaperPlatform : KelpPlatform {

    override fun wrapSender(rawSender: Any): Sender {
        val bukkitSender = rawSender as CommandSender
        return if (bukkitSender is Player)
            PaperPlayerSender(bukkitSender)
        else
            PaperSender(bukkitSender)
    }

    override fun hasPermission(rawSender: Any, permission: String): Boolean {
        if (permission.isBlank()) return true
        return (rawSender as? CommandSender)?.hasPermission(permission) ?: false
    }
}