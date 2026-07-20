package com.juliohaga.kelp.api.requirement

interface RequirementRegistry {
    fun <A : Annotation> register(type: Class<A>, handler: RequirementHandler<A>)
    fun resolve(annotations: List<Annotation>, context: RequirementContext): RequirementResult
}