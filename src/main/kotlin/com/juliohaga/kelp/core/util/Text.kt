package com.juliohaga.kelp.core.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.inventory.meta.ItemMeta
import java.text.Normalizer


private val legacySerializer = LegacyComponentSerializer
    .builder()
    .character('&')
    .hexColors()
    .build()

private val miniMessage = MiniMessage.miniMessage()


private val smallCapsMap = mapOf(
    'a' to 'ᴀ', 'b' to 'ʙ', 'c' to 'ᴄ', 'd' to 'ᴅ', 'e' to 'ᴇ',
    'f' to 'ꜰ', 'g' to 'ɢ', 'h' to 'ʜ', 'i' to 'ɪ', 'j' to 'ᴊ',
    'k' to 'ᴋ', 'l' to 'ʟ', 'm' to 'ᴍ', 'n' to 'ɴ', 'o' to 'ᴏ',
    'p' to 'ᴘ', 'q' to 'q', 'r' to 'ʀ', 's' to 's', 't' to 'ᴛ',
    'u' to 'ᴜ', 'v' to 'ᴠ', 'w' to 'ᴡ', 'x' to 'x', 'y' to 'ʏ',
    'z' to 'ᴢ'
)

private val diacriticsRegex = Regex("\\p{Mn}+")



fun String.colorize(): Component =
    legacySerializer.deserialize(this)
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



fun String.stripAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(diacriticsRegex, "")



fun String.toSmallCaps(): String =
    lowercase()
        .stripAccents()
        .map { smallCapsMap[it] ?: it }
        .joinToString("")