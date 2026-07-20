package com.juliohaga.kelp.api.requirement

import com.juliohaga.kelp.api.Sender

interface RequirementContext {
    val sender: Sender
    val identifier: String
}