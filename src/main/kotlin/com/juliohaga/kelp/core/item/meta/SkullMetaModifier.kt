package com.juliohaga.kelp.core.item.meta

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta


class SkullMetaModifier(
    private val action: SkullMeta.() -> Unit
) {


    fun apply(item: ItemStack) {

        item.editMeta(
            SkullMeta::class.java
        ) {

            it.action()

        }

    }

}