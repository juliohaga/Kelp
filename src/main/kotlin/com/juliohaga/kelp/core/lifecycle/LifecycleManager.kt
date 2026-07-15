package com.juliohaga.kelp.core.lifecycle

import com.juliohaga.kelp.core.component.Initializable
import com.juliohaga.kelp.core.di.DependencyContainer

class LifecycleManager(
    private val container: DependencyContainer
) {

    fun enable() {
        container
            .all()
            .filterIsInstance<Initializable>()
            .forEach(Initializable::onEnable)
    }

    fun disable() {
        container
            .all()
            .filterIsInstance<Initializable>()
            .forEach(Initializable::onDisable)
    }
}