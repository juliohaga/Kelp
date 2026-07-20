package com.juliohaga.kelp.api.requirement

sealed class RequirementResult {
    object Allowed : RequirementResult()
    data class Denied(val message: String) : RequirementResult()
}