package com.juliohaga.kelp.core.registry

import com.juliohaga.kelp.core.command.Command
import com.juliohaga.kelp.core.di.DependencyContainer
import com.juliohaga.kelp.core.di.InstanceFactory
import com.juliohaga.kelp.core.discovery.ComponentScanner
import org.bukkit.plugin.java.JavaPlugin

class CommandRegistry(
    private val plugin: JavaPlugin,
    private val container: DependencyContainer,
    private val scanner: ComponentScanner
) {

    fun register() {

        val factory = InstanceFactory(container)

        scanner.getCommands()
            .forEach {

                val command =
                    factory.create(it)
                            as Command


                plugin.getCommand(command.getCommand())
                    ?.apply {

                        setExecutor(command)

                        command.getTabCompleter()
                            ?.let {
                                tabCompleter = it
                            }
                    }
            }
    }
}