package com.juliohaga.kelp.core.actionbar

import org.bukkit.entity.Player

interface BarProvider {
    val id: String
    fun getBar(player: Player): BarState?
}