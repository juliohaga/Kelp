package com.juliohaga.kelp.api.text

/** Platform-independent text style. `color` accepts a hex string like "#FF00AA"
 *  or null to inherit whatever color is already active. */
data class TextStyle(
    val color: String? = null,
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underlined: Boolean = false,
    val strikethrough: Boolean = false,
    val obfuscated: Boolean = false
)