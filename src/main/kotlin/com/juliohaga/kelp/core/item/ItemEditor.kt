package com.juliohaga.kelp.core.item

import com.juliohaga.kelp.core.item.modifier.ItemModifier
import org.bukkit.inventory.ItemStack

class ItemEditor(
    private val item: ItemStack
) {

    fun modifier(
        modifier: ItemModifier
    ): ItemEditor {

        modifier.apply(item)

        return this
    }


    fun build(): CustomItem {

        return CustomItem(
            item.clone()
        )
    }
}