package com.juliohaga.kelp.api.text

sealed class TextClickAction {
    data class RunCommand(val command: String) : TextClickAction()
    data class SuggestCommand(val command: String) : TextClickAction()
    data class OpenUrl(val url: String) : TextClickAction()
}