package com.juliohaga.kelp.core.di

class DependencyContainer {

    private val instances = mutableMapOf<String, Any>()

    fun register(type: Class<*>, instance: Any) {
        instances[type.name] = instance
    }

    fun contains(type: Class<*>): Boolean {
        return instances.containsKey(type.name)
    }

    fun all(): Collection<Any> {
        return instances.values
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(type: Class<T>): T {
        return instances[type.name] as? T
            ?: error("Dependency ${type.simpleName} not found")
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getAll(type: Class<T>): List<T> {

        return instances.values
            .filter {
                type.isAssignableFrom(it.javaClass)
            }
            .map {
                it as T
            }

    }

}