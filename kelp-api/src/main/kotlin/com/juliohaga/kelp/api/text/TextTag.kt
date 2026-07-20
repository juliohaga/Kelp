package com.juliohaga.kelp.api.text

/** A text tag transforms the captured content of a &tagname marker into
 *  one or more styled nodes, before the message is rendered by the platform. */
fun interface TextTag {
    fun apply(content: String): List<TextNode>
}