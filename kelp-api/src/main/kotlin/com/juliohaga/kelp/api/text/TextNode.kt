package com.juliohaga.kelp.api.text


/** A single styled run of text. A tag can expand into one or many of these
 *  (e.g. smallcaps -> one node with transformed text, rainbow -> one node
 *  per character, each with a different color). */
data class TextNode(
    val text: String,
    val style: TextStyle? = null
)