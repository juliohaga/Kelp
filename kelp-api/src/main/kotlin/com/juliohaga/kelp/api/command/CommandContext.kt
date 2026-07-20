package com.juliohaga.kelp.api.command

import com.juliohaga.kelp.api.Sender
import com.juliohaga.kelp.api.requirement.RequirementContext

class CommandContext(
    override val sender: Sender,
    val args: Array<String>,
    val path: String
) : RequirementContext {

    override val identifier: String
        get() = path
}