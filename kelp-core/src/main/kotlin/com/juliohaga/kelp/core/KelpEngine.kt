package com.juliohaga.kelp.core

import com.juliohaga.kelp.api.Component
import com.juliohaga.kelp.api.StartupSummary
import com.juliohaga.kelp.api.requirement.types.PlayerOnly
import com.juliohaga.kelp.core.command.CommandExecutor
import com.juliohaga.kelp.core.command.CommandRegistry
import com.juliohaga.kelp.core.command.ParsedCommand
import com.juliohaga.kelp.core.di.KelpContainer
import com.juliohaga.kelp.core.lifecycle.LifecycleManager
import com.juliohaga.kelp.core.platform.KelpPlatform
import com.juliohaga.kelp.core.requirement.RequirementRegistryImpl
import com.juliohaga.kelp.core.requirement.handlers.PlayerOnlyHandler
import com.juliohaga.kelp.core.scanner.ClassScanner
import kotlin.reflect.KClass
import kotlin.time.measureTimedValue

/** Entry point of kelp-core. Instantiated once per plugin by the platform module (e.g. kelp-paper). */
class KelpEngine(
    classLoader: ClassLoader,
    basePackage: String,
    private val platform: KelpPlatform
) {
    private val scanner = ClassScanner(classLoader, basePackage)
    private val container = KelpContainer()
    private val lifecycle = LifecycleManager()
    private val requirementRegistry = RequirementRegistryImpl()

    lateinit var commands: List<ParsedCommand>
        private set
    lateinit var executor: CommandExecutor
        private set

    /** Lines contributed by every StartupSummary @Component, collected right
     *  after onEnable() runs on all components (so world/data loading etc
     *  has already happened by the time these are gathered). */
    var startupSummaries: List<String> = emptyList()
        private set

    /** Simple class names of every registered @Component, for the console banner. */
    var componentNames: List<String> = emptyList()
        private set

    /** Total time boot() took, in milliseconds. Useful for the "ready in Xms" banner line. */
    var bootDurationMs: Long = 0
        private set

    /**
     * @param configure Runs before @Component scanning, with direct access
     *   to the container — lets platform modules (e.g. kelp-paper) provide
     *   pre-built singletons (like the JavaPlugin instance itself) so any
     *   @Component can request them via constructor injection.
     */
    fun boot(configure: (KelpContainer) -> Unit = {}) {
        val (_, duration) = measureTimedValue {
            registerBuiltinRequirements()
            configure(container)

            val componentClasses = scanner.findAnnotatedWith(Component::class)
            container.registerAll(componentClasses)
            componentNames = componentClasses.map { it.simpleName ?: it.toString() }

            lifecycle.track(container.allInstances())

            commands = CommandRegistry(scanner).scan()
            executor = CommandExecutor(platform, container, requirementRegistry)

            lifecycle.enableAll()

            startupSummaries = container.allInstances()
                .filterIsInstance<StartupSummary>()
                .flatMap { it.summary() }
        }

        bootDurationMs = duration.inWholeMilliseconds
    }

    fun shutdown() = lifecycle.disableAll()

    /** Finds every class implementing [interfaceClass] on this plugin's
     *  classpath. Generic passthrough to the scanner — lets platform
     *  modules (e.g. kelp-paper) discover platform-specific types
     *  (org.bukkit.event.Listener) without kelp-core knowing they exist. */
    fun findImplementing(interfaceClass: KClass<*>): List<KClass<*>> =
        scanner.findImplementing(interfaceClass)

    private fun registerBuiltinRequirements() {
        requirementRegistry.register(PlayerOnly::class.java, PlayerOnlyHandler)
        // future built-in requirements (Cooldown, ConsoleOnly, ...) register here too
    }
}