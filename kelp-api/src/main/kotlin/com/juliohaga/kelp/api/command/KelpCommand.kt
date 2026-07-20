package com.juliohaga.kelp.api.command

import com.juliohaga.kelp.api.Sender

abstract class KelpCommand {

    protected lateinit var context: CommandContext
        private set

    /**
     * Called by the kelp-core dispatcher right before invoking a subcommand
     * method. Not meant to be called manually.
     */
    fun attachContext(context: CommandContext) {
        this.context = context
    }

    protected val sender: Sender
        get() = context.sender

    protected val args: Array<String>
        get() = context.args

    protected val path: String
        get() = context.path
}