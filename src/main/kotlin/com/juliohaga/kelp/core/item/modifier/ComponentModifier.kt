package com.juliohaga.kelp.core.item.modifier

import io.papermc.paper.datacomponent.DataComponentType
import org.bukkit.inventory.ItemStack


class ComponentModifier<T>(
    private val type: DataComponentType.Valued<T>,
    private val value: T
) : ItemModifier {


    override fun apply(item: ItemStack) {

        item.setData(
            type,
            value
        )

    }
}