package com.juliohaga.kelp.core.requirement

import com.juliohaga.kelp.api.requirement.Requirement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation

/**
 * Collects requirement annotations (any annotation meta-annotated with
 * @Requirement) present on a class and/or function, merging both.
 *
 * Used for command dispatch (class + subcommand method) and, later, for
 * event handler dispatch (class + handler method) — this collector has
 * no notion of "command" whatsoever, it just deals with KClass/KFunction.
 */
object RequirementCollector {

    fun collect(type: KClass<*>, function: KFunction<*>? = null): List<Annotation> {
        val classAnnotations = type.annotations.filter { it.isRequirement() }
        val functionAnnotations = function?.annotations?.filter { it.isRequirement() } ?: emptyList()
        return classAnnotations + functionAnnotations
    }

    private fun Annotation.isRequirement(): Boolean {
        return annotationClass.findAnnotation<Requirement>() != null
    }
}