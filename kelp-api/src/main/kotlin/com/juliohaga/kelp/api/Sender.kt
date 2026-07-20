package com.juliohaga.kelp.api

import com.juliohaga.kelp.api.text.Text

/**
 * Platform-independent sender abstraction.
 * Used by command execution, requirements, and (later) event handlers.
 *
 * Implementations are provided by platform modules (e.g. kelp-paper).
 */
interface Sender {
    fun sendMessage(message: String)
    fun sendMessage(text: Text)
}