package com.juliohaga.kelp.core.item.meta

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta


class PotionMetaModifier(
    private val action: PotionMeta.() -> Unit
) {


    fun apply(item: ItemStack) {

        item.editMeta(
            PotionMeta::class.java
        ) {

            it.action()

        }

    }

}