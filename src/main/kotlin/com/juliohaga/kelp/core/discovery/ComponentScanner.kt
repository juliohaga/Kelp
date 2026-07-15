package com.juliohaga.kelp.core.discovery

import com.juliohaga.kelp.core.component.Component
import com.juliohaga.kelp.core.di.DependencyContainer
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
                .addScanners(Scanners.TypesAnnotated, Scanners.SubTypes)
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


    fun getListeners() =
        reflections.getSubTypesOf(
            org.bukkit.event.Listener::class.java
        )


    fun getCommands() =
        reflections.getSubTypesOf(
            com.juliohaga.kelp.core.command.Command::class.java
        )
}