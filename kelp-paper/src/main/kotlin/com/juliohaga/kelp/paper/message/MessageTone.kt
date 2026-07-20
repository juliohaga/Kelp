package com.juliohaga.kelp.paper.message

import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound

enum class MessageTone(
    val colorCode: String,
    val color: NamedTextColor,
    val sound: Sound,
    val volume: Float = 1f,
    val pitch: Float = 1f,
) {

    SUCCESS(
        colorCode = "&a",
        color = NamedTextColor.GREEN,
        sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
        pitch = 1.4f
    ),

    DENIED(
        colorCode = "&c",
        color = NamedTextColor.RED,
        sound = Sound.BLOCK_NOTE_BLOCK_BASS,
        volume = 0.6f,
        pitch = 0.6f
    ),

    WARNING(
        colorCode = "&e",
        color = NamedTextColor.YELLOW,
        sound = Sound.BLOCK_NOTE_BLOCK_PLING,
        pitch = 1.2f
    ),

    INFO(
        colorCode = "&b",
        color = NamedTextColor.AQUA,
        sound = Sound.UI_BUTTON_CLICK,
        volume = 0.5f
    ),

    NEUTRAL(
        colorCode = "&7",
        color = NamedTextColor.GRAY,
        sound = Sound.UI_BUTTON_CLICK,
        volume = 0.3f
    )
}