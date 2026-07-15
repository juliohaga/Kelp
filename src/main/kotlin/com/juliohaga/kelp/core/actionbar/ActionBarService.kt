package com.juliohaga.kelp.core.actionbar

import com.juliohaga.kelp.core.component.Initializable
import com.juliohaga.kelp.core.component.Component
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

@Component
class ActionBarService(private val plugin: JavaPlugin) : Initializable {

    private val providers = mutableListOf<BarProvider>()

    fun register(provider: BarProvider) {
        providers += provider
    }

    override fun onEnable() {

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {

            val now = System.currentTimeMillis()

            Bukkit.getOnlinePlayers().forEach { player ->

                val states = providers.mapNotNull { it.getBar(player) }

                if (states.isEmpty()) return@forEach

                val locked = states.filter { it.lockUntil > now }

                val chosen = locked.maxByOrNull { it.priority }
                    ?: states.maxByOrNull { it.priority }

                chosen?.let { player.sendActionBar(it.component) }
            }

        }, 2L, 2L)
    }
}