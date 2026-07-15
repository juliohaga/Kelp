package com.juliohaga.kelp.core.actionbar

import net.kyori.adventure.text.Component

data class BarState(
    val component: Component,
    val priority: Int = 0,
    val lockUntil: Long = 0L
)
