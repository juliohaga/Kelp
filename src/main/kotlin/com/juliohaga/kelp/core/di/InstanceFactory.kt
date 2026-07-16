package com.juliohaga.kelp.core.di

import com.juliohaga.kelp.core.component.Component
import com.juliohaga.kelp.core.discovery.ComponentScanner
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

class InstanceFactory(
    private val container: DependencyContainer,
    private val scanner: ComponentScanner
) {

    fun <T> create(clazz: Class<T>): T {

        if (container.contains(clazz)) {
            return container.get(clazz)
        }

        val constructor = clazz.constructors.firstOrNull()
            ?: error("Constructor not found: ${clazz.simpleName}")

        val args = constructor.genericParameterTypes.mapIndexed { index, genericType ->

            val rawType = constructor.parameterTypes[index]

            when {

                rawType == List::class.java -> {

                    val parameterized =
                        genericType as ParameterizedType

                    val elementType =
                        resolveClass(parameterized.actualTypeArguments[0])

                    scanner
                        .getComponentsOf(elementType)
                        .forEach { component ->

                            if (!container.contains(component)) {
                                create(component)
                            }

                        }

                    container.getAll(elementType)
                }

                container.contains(rawType) -> {

                    container.get(rawType)

                }

                isComponent(rawType) -> {

                    create(rawType)

                }

                else -> {

                    error(
                        "Missing dependency ${rawType.simpleName} in ${clazz.simpleName}"
                    )

                }

            }

        }.toTypedArray()

        val instance = constructor.newInstance(*args)

        container.register(clazz, instance)

        @Suppress("UNCHECKED_CAST")
        return instance as T
    }

    private fun resolveClass(type: Type): Class<*> {
        return when (type) {
            is Class<*> -> type
            is ParameterizedType -> resolveClass(type.rawType)
            is WildcardType -> {
                // pega o upper bound (? extends Foo -> Foo)
                val upper = type.upperBounds.firstOrNull()
                    ?: error("Wildcard type has no resolvable upper bound $type")
                resolveClass(upper)
            }
            else -> error("Generic type not suported: $type")
        }
    }

    private fun isComponent(type: Class<*>): Boolean {

        return type.isAnnotationPresent(Component::class.java)

    }

}