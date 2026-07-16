package com.juliohaga.kelp.core

import com.juliohaga.kelp.core.di.InstanceFactory
import com.juliohaga.kelp.core.discovery.ComponentScanner
import com.juliohaga.kelp.core.lifecycle.LifecycleManager
import com.juliohaga.kelp.core.registry.CommandRegistry
import com.juliohaga.kelp.core.registry.ListenerRegistry
import org.bukkit.plugin.java.JavaPlugin

class Bootstrap(
    private val plugin: JavaPlugin
) {

    private lateinit var lifecycle: LifecycleManager

    fun start() {

        val scanner = ComponentScanner(plugin)

        val container = scanner.createContainer()

        val instanceFactory = InstanceFactory(
            container,
            scanner
        )

        scanner.getComponents()
            .forEach { componentClass ->

                if (!container.contains(componentClass)) {
                    instanceFactory.create(componentClass)
                }

            }


        lifecycle = LifecycleManager(container)

        lifecycle.enable()


        ListenerRegistry(
            plugin,
            container,
            scanner,
            instanceFactory
        ).register()


        CommandRegistry(
            plugin,
            container,
            scanner,
            instanceFactory
        ).register()
    }


    fun stop() {

        if (!::lifecycle.isInitialized) {
            return
        }

        lifecycle.disable()
    }

}