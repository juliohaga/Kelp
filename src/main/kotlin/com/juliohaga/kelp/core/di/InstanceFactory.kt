package com.juliohaga.kelp.core.di

import com.juliohaga.kelp.core.component.Component

class InstanceFactory(
    private val container: DependencyContainer
) {

    fun <T> create(clazz: Class<T>): T {

        if (container.contains(clazz)) {
            return container.get(clazz)
        }

        val constructor = clazz.constructors.firstOrNull()
            ?: error("Constructor not found: ${clazz.simpleName}")


        val args = constructor.parameterTypes.map {

            when {
                container.contains(it) ->
                    container.get(it)

                isComponent(it) ->
                    create(it)

                else ->
                    error(
                        "Missing dependency ${it.simpleName} in ${clazz.simpleName}"
                    )
            }

        }.toTypedArray()


        val instance = constructor.newInstance(*args)

        container.register(clazz, instance)

        return instance as T
    }


    private fun isComponent(type: Class<*>): Boolean {

        return type.annotations.any {
            it.annotationClass.java.name == Component::class.java.name
        }
    }
}