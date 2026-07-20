package com.juliohaga.kelp.api.text

/**
 * Platform-independent rich text message. Supports &tagname<content>-style
 * tags (see TextTags) for formatting beyond plain legacy color codes, plus
 * hover, click actions, and composition via append() — each child keeps
 * its own independent hover/click, letting a single message have multiple
 * clickable/hoverable fragments.
 */
class Text private constructor(
    val raw: String,
    val hoverText: Text? = null,
    val click: TextClickAction? = null,
    val children: List<Text> = emptyList()
) {

    companion object {
        fun of(text: String) = Text(text)
    }

    fun hover(text: Text): Text = Text(raw, text, click, children)
    fun hover(text: String): Text = hover(of(text))

    fun runCommand(command: String): Text = Text(raw, hoverText, TextClickAction.RunCommand(command), children)
    fun suggestCommand(command: String): Text = Text(raw, hoverText, TextClickAction.SuggestCommand(command), children)
    fun openUrl(url: String): Text = Text(raw, hoverText, TextClickAction.OpenUrl(url), children)

    /** Appends another Text as a child, preserving that child's own
     *  hover/click independently of this Text's. */
    fun append(child: Text): Text = Text(raw, hoverText, click, children + child)
}