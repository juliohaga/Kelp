package com.juliohaga.kelp.core.discovery

import com.juliohaga.kelp.core.command.Command
import com.juliohaga.kelp.core.component.Component
import com.juliohaga.kelp.core.di.DependencyContainer
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder

class ComponentScanner(
    private val plugin: JavaPlugin
) {

    private val classLoader =
        plugin.javaClass.classLoader

    private val reflections =
        Reflections(
            ConfigurationBuilder()
                .setUrls(
                    listOf(
                        plugin.javaClass.protectionDomain.codeSource.location
                    )
                )
                .addClassLoaders(classLoader)
                .addScanners(
                    Scanners.TypesAnnotated,
                    Scanners.SubTypes
                )
        )

    fun createContainer(): DependencyContainer {

        val container = DependencyContainer()

        container.register(JavaPlugin::class.java, plugin)
        container.register(Plugin::class.java, plugin)

        return container
    }

    fun getComponents(): Set<Class<*>> {

        return reflections
            .getTypesAnnotatedWith(Component::class.java)

    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getComponentsOf(type: Class<T>): Set<Class<out T>> {

        return reflections
            .getSubTypesOf(type)
            .filter {
                it.isAnnotationPresent(Component::class.java)
            }
            .map {
                it as Class<out T>
            }
            .toSet()

    }

    fun getListeners(): Set<Class<out Listener>> {

        return reflections.getSubTypesOf(
            Listener::class.java
        )

    }

    fun getCommands(): Set<Class<out Command>> {

        return reflections.getSubTypesOf(
            Command::class.java
        )

    }

}