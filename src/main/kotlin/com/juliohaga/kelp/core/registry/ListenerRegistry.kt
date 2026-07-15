package com.juliohaga.kelp.core.registry

import com.juliohaga.kelp.core.di.DependencyContainer
import com.juliohaga.kelp.core.di.InstanceFactory
import com.juliohaga.kelp.core.discovery.ComponentScanner
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class ListenerRegistry(
    private val plugin: JavaPlugin,
    private val container: DependencyContainer,
    private val scanner: ComponentScanner
) {

    fun register() {

        val factory = InstanceFactory(container)

        scanner.getListeners()
            .forEach {

                val listener =
                    factory.create(it)
                            as Listener

                plugin.server.pluginManager
                    .registerEvents(
                        listener,
                        plugin
                    )
            }
    }
}