package com.juliohaga.kelp.api

/**
 * Implemented by @Component classes that want to contribute a line (or a
 * few) to the banner printed to console once the engine finishes booting —
 * e.g. "Loaded 3 worlds (7 dimensions)".
 *
 * Purely informational: has no effect on boot order, dependency resolution,
 * or command registration. Collected after every Initializable.onEnable()
 * has already run, so it's safe to report on state set up during onEnable.
 */
interface StartupSummary {
    fun summary(): List<String>
}