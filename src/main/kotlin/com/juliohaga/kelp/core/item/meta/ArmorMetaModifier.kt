package com.juliohaga.kelp.core.item.meta

import com.juliohaga.kelp.core.item.modifier.ItemModifier
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta


class ArmorMetaModifier(
    private val action: ArmorMeta.() -> Unit
) : ItemModifier {

    override fun apply(item: ItemStack) {

        item.editMeta(
            ArmorMeta::class.java
        ) {

            it.action()

        }

    }
}