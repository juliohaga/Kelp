package com.juliohaga.kelp.core.requirement

import com.juliohaga.kelp.api.requirement.RequirementContext
import com.juliohaga.kelp.api.requirement.RequirementHandler
import com.juliohaga.kelp.api.requirement.RequirementRegistry
import com.juliohaga.kelp.api.requirement.RequirementResult

class RequirementRegistryImpl : RequirementRegistry {

    private val handlers = mutableMapOf<Class<out Annotation>, RequirementHandler<Annotation>>()

    override fun <A : Annotation> register(type: Class<A>, handler: RequirementHandler<A>) {
        @Suppress("UNCHECKED_CAST")
        handlers[type] = handler as RequirementHandler<Annotation>
    }

    override fun resolve(annotations: List<Annotation>, context: RequirementContext): RequirementResult {
        for (annotation in annotations) {
            val handler = handlers[annotation.annotationClass.java] ?: continue
            val result = handler.check(annotation, context)
            if (result is RequirementResult.Denied) return result
        }
        return RequirementResult.Allowed
    }
}
