package com.juliohaga.kelp.paper.command

import com.juliohaga.kelp.api.PlayerSender
import org.bukkit.entity.Player

/** PaperSender specialization for players — this is the type that makes
 *  @PlayerOnly resolvable in kelp-core without any Bukkit import there:
 *  PlayerOnlyHandler just checks `sender is PlayerSender`. */
class PaperPlayerSender(val player: Player) : PaperSender(player), PlayerSender