package com.juliohaga.kelp.paper.item

import com.juliohaga.kelp.paper.text.colorize
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.meta.ItemMeta

private val legacySerializer = LegacyComponentSerializer
    .builder()
    .character('&')
    .hexColors()
    .build()

fun ItemMeta.applyName(
    text: String,
    italic: Boolean = false
) {
    val component = if (italic) {
        legacySerializer.deserialize(text)
    } else {
        text.colorize()
    }

    itemName(component)
    displayName(component)
}