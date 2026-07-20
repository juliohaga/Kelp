package com.juliohaga.kelp.paper

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Coordinates the startup banner across multiple Kelp-based plugins running
 * in the same JVM. Since kelp-paper is shaded into each plugin's own jar
 * (own classloader, own copy of every Kelp class), a normal Kotlin `object`
 * singleton would NOT be shared between plugins — each would see its own
 * isolated instance. System.getProperties() is the one place that's
 * genuinely shared across the whole JVM regardless of classloader, so it's
 * used here as the coordination point. Only JDK types (String, List, Long)
 * are stored — never a Kelp class instance — since two different plugins'
 * shaded copies of the same class are technically different types to the
 * JVM, and casting between them would throw ClassCastException.
 */
object KelpBannerRegistry {

    private const val CONTRIBUTIONS_KEY = "kelp.banner.contributions"
    private const val PRINTER_CLAIMED_KEY = "kelp.banner.printer_claimed"

    @Suppress("UNCHECKED_CAST")
    private fun contributionsList(): CopyOnWriteArrayList<Map<String, Any>> {
        return System.getProperties().computeIfAbsent(CONTRIBUTIONS_KEY) {
            CopyOnWriteArrayList<Map<String, Any>>()
        } as CopyOnWriteArrayList<Map<String, Any>>
    }

    /** Adds this plugin's contribution to the shared registry. Safe to call
     *  from any plugin's onEnable(), in any order. */
    fun contribute(
        pluginName: String,
        commandNames: List<String>,
        componentNames: List<String>,
        listenerNames: List<String>,
        bootDurationMs: Long
    ) {
        contributionsList() += mapOf(
            "plugin" to pluginName,
            "commands" to commandNames,
            "components" to componentNames,
            "listeners" to listenerNames,
            "bootDurationMs" to bootDurationMs
        )
    }

    /** Returns true only for the first caller across every Kelp-based
     *  plugin in this JVM — that caller is responsible for scheduling and
     *  printing the combined banner. Every other plugin just contributes
     *  and returns false, printing nothing itself. */
    fun claimPrinter(): Boolean {
        return System.getProperties().putIfAbsent(PRINTER_CLAIMED_KEY, true) == null
    }

    fun allContributions(): List<Map<String, Any>> = contributionsList().toList()
}