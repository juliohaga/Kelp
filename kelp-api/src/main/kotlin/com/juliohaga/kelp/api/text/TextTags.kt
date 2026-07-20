package com.juliohaga.kelp.api.text

import java.text.Normalizer

private val diacriticsRegex = Regex("\\p{Mn}+")

private val smallCapsMap = mapOf(
    'a' to 'ᴀ', 'b' to 'ʙ', 'c' to 'ᴄ', 'd' to 'ᴅ', 'e' to 'ᴇ',
    'f' to 'ꜰ', 'g' to 'ɢ', 'h' to 'ʜ', 'i' to 'ɪ', 'j' to 'ᴊ',
    'k' to 'ᴋ', 'l' to 'ʟ', 'm' to 'ᴍ', 'n' to 'ɴ', 'o' to 'ᴏ',
    'p' to 'ᴘ', 'q' to 'q', 'r' to 'ʀ', 's' to 's', 't' to 'ᴛ',
    'u' to 'ᴜ', 'v' to 'ᴠ', 'w' to 'ᴡ', 'x' to 'x', 'y' to 'ʏ',
    'z' to 'ᴢ'
)

private fun String.stripAccents(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(diacriticsRegex, "")

/** Public small-caps transform, reused both by the &s/§s text tag and by
 *  anything outside the Text pipeline (e.g. the console startup banner)
 *  that wants the same visual style without going through Text/TextTags. */
fun String.toSmallCaps(): String =
    lowercase().stripAccents().map { smallCapsMap[it] ?: it }.joinToString("")

/**
 * Registry of text tags (&tagname<content> markers). Tag names must not
 * collide with vanilla legacy color codes (0-9, a-f, k-o, r) — pick names
 * outside that set (e.g. "s" is safe, "a" is not).
 *
 * Register your own:
 *   KelpTextTags.register("rainbow") { content ->
 *       content.mapIndexed { i, c -> KelpNode(c.toString(), KelpStyle(color = rainbowHexFor(i))) }
 *   }
 */
object TextTags {

    private val tags = mutableMapOf<String, TextTag>()

    init {
        register("s") { content -> listOf(TextNode(content.toSmallCaps())) }
    }

    fun register(name: String, tag: TextTag) {
        require(name.isNotBlank()) { "Tag name cannot be blank" }
        tags[name] = tag
    }

    fun get(name: String): TextTag? = tags[name]
    fun names(): Set<String> = tags.keys
}