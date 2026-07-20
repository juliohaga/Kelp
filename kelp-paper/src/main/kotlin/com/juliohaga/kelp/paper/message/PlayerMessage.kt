package com.juliohaga.kelp.paper.message

import com.juliohaga.kelp.api.text.Text
import com.juliohaga.kelp.api.text.TextClickAction
import com.juliohaga.kelp.paper.text.TextRenderer
import org.bukkit.entity.Player

fun Player.message(text: String) {
    sendMessage(TextRenderer.render(Text.of(text)))
}

fun Player.message(text: Text) {
    sendMessage(TextRenderer.render(text))
}

fun Player.playMessage(
    tone: MessageTone,
    text: String
) {
    playMessage(tone, Text.of(text))
}

fun Player.playMessage(
    tone: MessageTone,
    text: Text
) {
    sendMessage(TextRenderer.render(text.withToneColor(tone)))

    playSound(
        location,
        tone.sound,
        tone.volume,
        tone.pitch
    )
}

/** Prefixes the tone's default color, preserving any hover/click already
 *  set on the original Text (which would otherwise get lost, since Text
 *  is reconstructed with a new raw string under the hood). Also preserves
 *  any children already appended to the original Text. */
private fun Text.withToneColor(tone: MessageTone): Text {
    var toned = Text.of(tone.colorCode + raw)

    hoverText?.let { toned = toned.hover(it) }

    when (val action = click) {
        is TextClickAction.RunCommand -> toned = toned.runCommand(action.command)
        is TextClickAction.SuggestCommand -> toned = toned.suggestCommand(action.command)
        is TextClickAction.OpenUrl -> toned = toned.openUrl(action.url)
        null -> {}
    }

    children.forEach { child ->
        toned = toned.append(child)
    }

    return toned
}