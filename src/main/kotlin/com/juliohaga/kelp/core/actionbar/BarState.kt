package com.juliohaga.kelp.core.actionbar

import net.kyori.adventure.text.Component

data class BarState(
    val component: Component,
    val priority: Int = 0,       // usado quando duas bars "normais" competem ao mesmo tempo
    val lockUntil: Long = 0L     // enquanto > now, essa bar tem exclusividade (ex: animação de ganho de energia)
)
