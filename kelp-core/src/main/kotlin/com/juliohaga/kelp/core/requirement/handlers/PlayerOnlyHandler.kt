package com.juliohaga.kelp.core.requirement.handlers

import com.juliohaga.kelp.api.PlayerSender
import com.juliohaga.kelp.api.requirement.RequirementContext
import com.juliohaga.kelp.api.requirement.RequirementHandler
import com.juliohaga.kelp.api.requirement.RequirementResult
import com.juliohaga.kelp.api.requirement.types.PlayerOnly

object PlayerOnlyHandler : RequirementHandler<PlayerOnly> {
    override fun check(annotation: PlayerOnly, context: RequirementContext): RequirementResult {
        return if (context.sender !is PlayerSender)
            RequirementResult.Denied("§cThis command can only be executed by players.")
        else
            RequirementResult.Allowed
    }

}