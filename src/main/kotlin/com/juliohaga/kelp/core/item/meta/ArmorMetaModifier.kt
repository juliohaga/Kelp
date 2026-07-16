package com.juliohaga.kelp.core.item.meta

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta


class ArmorMetaModifier(
    private val action: ArmorMeta.() -> Unit
) {


    fun apply(item: ItemStack) {

        item.editMeta(
            ArmorMeta::class.java
        ) {

            it.action()

        }

    }

}