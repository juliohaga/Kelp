package com.juliohaga.kelp.core.lifecycle

import com.juliohaga.kelp.api.Initializable

class LifecycleManager {

    private val tracked = mutableListOf<Initializable>()

    fun track(instances: Collection<Any>) {
        tracked += instances.filterIsInstance<Initializable>()
    }

    fun enableAll() = tracked.forEach { it.onEnable() }

    /** Disables in reverse order, mirroring how dependencies are usually torn down. */
    fun disableAll() = tracked.asReversed().forEach { it.onDisable() }
}