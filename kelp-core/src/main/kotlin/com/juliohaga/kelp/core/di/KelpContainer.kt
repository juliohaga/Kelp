package com.juliohaga.kelp.core.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class KelpContainer {

    private val instances = mutableMapOf<KClass<*>, Any>()

    // every class known to be a @Component, regardless of whether it has
    // been instantiated yet — lets instantiate() resolve dependencies
    // out of scan order instead of requiring them to be registered first
    private var known: Set<KClass<*>> = emptySet()

    /** Directly registers a pre-built singleton under [kClass] — used for
     *  platform-provided instances (e.g. the JavaPlugin itself) that other
     *  @Component classes can request via constructor injection, without
     *  the container needing to know how to construct them via reflection.
     *  Call before registerAll()/scanning, so dependents resolve it fine. */
    fun <T : Any> provide(kClass: KClass<T>, instance: T) {
        instances[kClass] = instance
    }

    /** Registers every @Component class found on the classpath, resolving
     *  dependencies between them regardless of the order they were scanned in. */
    fun registerAll(classes: List<KClass<*>>) {
        known = classes.toSet()
        classes.forEach(::register)
    }

    /** Registers and instantiates a @Component class, resolving its constructor by type. */
    fun register(kClass: KClass<*>) {
        if (instances.containsKey(kClass)) return // already resolved, avoids reprocessing on a cycle
        instances[kClass] = instantiate(kClass)
    }

    fun <T : Any> get(kClass: KClass<T>): T {
        @Suppress("UNCHECKED_CAST")
        return instances[kClass] as? T
            ?: throw DependencyResolutionException("No @Component registered for ${kClass.simpleName}")
    }

    fun allInstances(): Collection<Any> = instances.values

    /** Creates a new instance (not stored in the container) resolving its constructor by type.
     *  Used for KelpCommand, which is not a singleton. */
    fun instantiate(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor
            ?: throw DependencyResolutionException("${kClass.simpleName} has no primary constructor")

        val args = constructor.parameters.associateWith { param ->
            val paramClass = param.type.classifier as? KClass<*>
                ?: throw DependencyResolutionException("Invalid parameter in ${kClass.simpleName}")

            instances[paramClass]
                ?: resolveKnownDependency(paramClass)
                ?: throw DependencyResolutionException(
                    "${kClass.simpleName} depends on ${paramClass.simpleName}, but it is not registered as @Component"
                )
        }

        return constructor.callBy(args)
    }

    /** If [paramClass] is a known @Component that just hasn't been
     *  instantiated yet (because it was scanned after its dependent),
     *  register it now, on demand. */
    private fun resolveKnownDependency(paramClass: KClass<*>): Any? {
        if (paramClass !in known) return null
        register(paramClass)
        return instances[paramClass]
    }
}