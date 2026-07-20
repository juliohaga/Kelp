package com.juliohaga.kelp.paper.text

import com.juliohaga.kelp.api.text.Text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage

private val miniMessage = MiniMessage.miniMessage()

fun String.colorize(): Component =
    TextRenderer.render(Text.of(this))
        .decoration(TextDecoration.ITALIC, false)

fun String.mini(): Component =
    miniMessage.deserialize(this)
        .decoration(TextDecoration.ITALIC, false)

fun Component.noItalic(): Component =
    decoration(TextDecoration.ITALIC, false)

fun List<String>.colorize(): List<Component> =
    map { it.colorize() }

fun List<String>.colorizeNoItalic(): List<Component> =
    map { it.colorize().noItalic() }