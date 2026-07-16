package com.juliohaga.kelp.core.message

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Sound
import org.bukkit.entity.Player


enum class MessageTone(
    val color: NamedTextColor,
    val sound: Sound,
    val volume: Float = 1f,
    val pitch: Float = 1f,
) {

    SUCCESS(
        color = NamedTextColor.GREEN,
        sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP,
        pitch = 1.4f
    ),

    DENIED(
        color = NamedTextColor.RED,
        sound = Sound.BLOCK_NOTE_BLOCK_BASS,
        volume = 0.6f,
        pitch = 0.6f
    ),

    WARNING(
        color = NamedTextColor.YELLOW,
        sound = Sound.BLOCK_NOTE_BLOCK_PLING,
        pitch = 1.2f
    ),

    INFO(
        color = NamedTextColor.AQUA,
        sound = Sound.UI_BUTTON_CLICK,
        volume = 0.5f
    ),

    NEUTRAL(
        color = NamedTextColor.GRAY,
        sound = Sound.UI_BUTTON_CLICK,
        volume = 0.3f
    )
}


fun Player.playMessage(
    tone: MessageTone,
    text: String
) {

    val component = Component.text(text)
        .color(tone.color)

    sendMessage(component)

    playSound(
        location,
        tone.sound,
        tone.volume,
        tone.pitch
    )
}


fun Player.playMessage(
    tone: MessageTone,
    component: Component
) {

    sendMessage(component)

    playSound(
        location,
        tone.sound,
        tone.volume,
        tone.pitch
    )
}