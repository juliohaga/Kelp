package com.juliohaga.kelp.paper

import com.juliohaga.kelp.core.KelpEngine
import com.juliohaga.kelp.paper.command.PaperCommandBridge
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

abstract class KelpPlugin : JavaPlugin() {

    private lateinit var engine: KelpEngine
    private var listenerNames: List<String> = emptyList()

    override fun onEnable() {
        engine = KelpEngine(
            classLoader = javaClass.classLoader,
            basePackage = javaClass.`package`.name,
            platform = PaperPlatform()
        )

        // provides the plugin instance itself, so any @Component can request
        // Plugin (or JavaPlugin) via constructor injection — e.g. to use
        // Bukkit.getScheduler() for delayed/repeating tasks
        engine.boot { container ->
            container.provide(Plugin::class, this)
            container.provide(JavaPlugin::class, this)
        }

        registerListeners()

        val bridge = PaperCommandBridge(engine.executor)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val registrar = event.registrar()
            engine.commands.forEach { command ->
                registrar.register(
                    bridge.build(command),
                    command.permission,
                    command.aliases
                )
            }

            contributeAndMaybePrintBanner()
        }
    }

    override fun onDisable() {
        if (::engine.isInitialized) engine.shutdown()
    }

    /**
     * Auto-registers every class on this plugin's classpath that implements
     * Listener — no annotation needed, since Bukkit already requires the
     * Listener interface to call registerEvents(). Instances are built via
     * the same DI container used for commands/components, so listeners get
     * constructor injection of any @Component for free.
     */
    private fun registerListeners() {

        val listenerClasses = engine.findImplementing(Listener::class)
        val registered = mutableListOf<String>()

        listenerClasses.forEach { kClass ->
            try {
                val instance = engine.executor.instantiate(kClass) as? Listener ?: return@forEach
                server.pluginManager.registerEvents(instance, this)
                registered += kClass.simpleName ?: kClass.toString()
            } catch (e: Exception) {
                logger.warning("Failed to register listener ${kClass.simpleName}: ${e.message}")
            }
        }

        listenerNames = registered
    }

    /**
     * Every Kelp-based plugin contributes its own summary data to the shared
     * KelpBannerRegistry. Only the FIRST plugin to reach this point actually
     * schedules the banner print (a few ticks later, so every other plugin
     * enabling in the same startup has had time to contribute too) — so
     * the ASCII art/banner is shown exactly once per server start, no
     * matter how many Kelp-based plugins are installed.
     */
    private fun contributeAndMaybePrintBanner() {

        KelpBannerRegistry.contribute(
            pluginName = name,
            commandNames = engine.commands.map { it.name },
            componentNames = engine.componentNames,
            listenerNames = listenerNames,
            bootDurationMs = engine.bootDurationMs
        )

        if (!KelpBannerRegistry.claimPrinter()) return

        // small delay so every other Kelp plugin enabling during this same
        // server startup has finished contributing before we read the list
        Bukkit.getScheduler().runTaskLater(this, Runnable {
            printBanner(KelpBannerRegistry.allContributions())
        }, 5L)
    }

    /**
     * Prints the Kelp banner (pre-built ASCII art: wordmark + kelp plant art
     * combined line-by-line) followed by a combined startup summary across
     * every Kelp-based plugin that contributed. Colored (§a for the banner,
     * §e for summary topics, §r for summary data) — sent via
     * Bukkit.getConsoleSender().sendMessage() instead of logger.info(),
     * since only the ConsoleSender pipeline reliably translates § color codes
     * on the Paper console, regardless of terminal/Jansi detection.
     */
    private fun printBanner(contributions: List<Map<String, Any>>) {

        val console = Bukkit.getConsoleSender()
        val green = "§a"
        val yellow = "§e"
        val reset = "§r"
        val divider = "$green${"─".repeat(70)}$reset"

        fun line(text: String = "") = console.sendMessage(text)
        fun blank() = console.sendMessage("")

        val banner = listOf(
            " █████   ████ ██████████ █████       █████████████                  ####",
            "░░███   ███░ ░░███░░░░░█░░███       ░░███░░░░░███               *****##",
            " ░███  ███    ░███  █ ░  ░███        ░███    ░███         ******===+*##",
            " ░███████     ░██████    ░███        ░██████████     *****+=======**=##",
            " ░███░░███    ░███░░█    ░███        ░███░░░░░░    ##=======******===@@",
            " ░███ ░░███   ░███ ░   █ ░███      █ ░███        ##======***=======+@",
            " █████ ░░████ ██████████ ███████████ █████     #**++**++===++##@@@@",
            "░░░░░   ░░░░ ░░░░░░░░░░ ░░░░░░░░░░░ ░░░░░    ###**##****=+*@@",
            "                                            #####******#%@",
            "                                            #######****@@",
            "                                            ###########@@",
            " Kelp v1.0.0                                 @@#######@@",
            " © 2026 Julio Haga                             @@@@@@@"
        )

        blank()
        banner.forEach { row -> line("$green$row$reset") }
        blank()

        line(divider)
        line("$yellow  Initialization completed successfully$reset")
        line(divider)
        blank()

        @Suppress("UNCHECKED_CAST")
        fun stringList(contribution: Map<String, Any>, key: String): List<String> =
            contribution[key] as? List<String> ?: emptyList()

        line("$yellow Plugins(${contributions.size}):$reset")
        line("$green • $reset${contributions.joinToString("$green, $reset") { it["plugin"] as? String ?: "?" }}")
        blank()

        val allCommands = contributions.flatMap { stringList(it, "commands") }.sorted()
        if (allCommands.isNotEmpty()) {
            line("$yellow Commands(${allCommands.size}):$reset")
            line("$green • $reset${allCommands.joinToString("$green, $reset")}")
            blank()
        }

        val worlds = Bukkit.getWorlds()
        if (worlds.isNotEmpty()) {
            val grouped = worlds.groupBy { it.key.namespace }
            line("$yellow Worlds(${grouped.size}):$reset")
            blank()
            grouped.entries.sortedBy { it.key }.forEach { (namespace, dimensions) ->
                val dims = dimensions.joinToString("$green, $reset") { it.key.value() }
                line("$green • $reset$namespace: $dims")
            }
            blank()
        }

        val allComponents = contributions.flatMap { stringList(it, "components") }.sorted()
        if (allComponents.isNotEmpty()) {
            line("$yellow Components(${allComponents.size}):$reset")
            line("$green • $reset${allComponents.joinToString("$green, $reset")}")
            blank()
        }

        val allListeners = contributions.flatMap { stringList(it, "listeners") }.sorted()
        if (allListeners.isNotEmpty()) {
            line("$yellow Listeners(${allListeners.size}):$reset")
            line("$green • $reset${allListeners.joinToString("$green, $reset")}")
            blank()
        }

        val totalBootMs = contributions.sumOf { (it["bootDurationMs"] as? Long) ?: 0L }

        line(divider)
        line("$yellow Kelp is ready! $reset($green${totalBootMs}ms$reset)")
        blank()
    }
}