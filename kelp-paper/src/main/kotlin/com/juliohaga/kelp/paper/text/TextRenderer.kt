package com.juliohaga.kelp.paper.text

import com.juliohaga.kelp.api.text.Text
import com.juliohaga.kelp.api.text.TextClickAction
import com.juliohaga.kelp.api.text.TextNode
import com.juliohaga.kelp.api.text.TextTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/** The only place in kelp-paper (and the whole codebase) that imports
 *  net.kyori.adventure directly. Converts a Text into a real Component. */
object TextRenderer {

    private val legacy = LegacyComponentSerializer.builder()
        .character('&')
        .hexColors()
        .build()

    fun render(text: Text): Component {
        var component = renderRaw(text.raw)

        text.hoverText?.let {
            component = component.hoverEvent(HoverEvent.showText(renderRaw(it.raw)))
        }

        when (val action = text.click) {
            is TextClickAction.RunCommand -> component = component.clickEvent(ClickEvent.runCommand(action.command))
            is TextClickAction.SuggestCommand -> component = component.clickEvent(ClickEvent.suggestCommand(action.command))
            is TextClickAction.OpenUrl -> component = component.clickEvent(ClickEvent.openUrl(action.url))
            null -> {}
        }

        // each child renders (and keeps) its own hover/click independently
        text.children.forEach { child ->
            component = component.append(render(child))
        }

        return component
    }

    private fun tagPattern(): Regex? {
        val names = TextTags.names()
        if (names.isEmpty()) return null

        val alternation = names
            .sortedByDescending { it.length }
            .joinToString("|") { Regex.escape(it) }

        return Regex("[&§]($alternation)([^&§]*)(?=[&§]|$)")
    }

    private fun renderRaw(raw: String): Component {
        val remaining = raw.replace('§', '&')
        val pattern = tagPattern() ?: return legacy.deserialize(remaining)

        var result = Component.empty()
        var lastEnd = 0
        var currentColor: TextColor? = null

        for (match in pattern.findAll(remaining)) {
            val tagName = match.groupValues[1]
            val handler = TextTags.get(tagName) ?: continue

            val legacySegment = legacy.deserialize(remaining.substring(lastEnd, match.range.first))
            currentColor = lastColorOf(legacySegment) ?: currentColor
            result = result.append(legacySegment)

            val content = match.groupValues[2]
            handler.apply(content).forEach { node ->
                val nodeComponent = toComponent(node, currentColor)
                currentColor = lastColorOf(nodeComponent) ?: currentColor
                result = result.append(nodeComponent)
            }

            lastEnd = match.range.last + 1
        }

        val trailingSegment = legacy.deserialize(remaining.substring(lastEnd))
        currentColor = lastColorOf(trailingSegment) ?: currentColor
        result = result.append(trailingSegment)

        return result
    }

    /** Walks to the rightmost leaf of a component tree to find the last
     *  color that was actively set, so it can be carried into the next
     *  fragment (tag node or legacy segment) that doesn't define its own. */
    private fun lastColorOf(component: Component): TextColor? {
        val children = component.children()
        if (children.isNotEmpty()) {
            return lastColorOf(children[children.size - 1]) ?: component.style().color()
        }
        return component.style().color()
    }

    private fun toComponent(node: TextNode, inheritedColor: TextColor?): Component {
        var component = Component.text(node.text)
        val style = node.style

        val color = style?.color?.let { TextColor.fromHexString(it) ?: NamedTextColor.WHITE } ?: inheritedColor
        color?.let { component = component.color(it) }

        if (style?.bold == true) component = component.decorate(TextDecoration.BOLD)
        if (style?.italic == true) component = component.decorate(TextDecoration.ITALIC)
        if (style?.underlined == true) component = component.decorate(TextDecoration.UNDERLINED)
        if (style?.strikethrough == true) component = component.decorate(TextDecoration.STRIKETHROUGH)
        if (style?.obfuscated == true) component = component.decorate(TextDecoration.OBFUSCATED)

        return component
    }
}