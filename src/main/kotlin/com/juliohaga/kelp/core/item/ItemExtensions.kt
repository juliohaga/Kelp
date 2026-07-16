package com.juliohaga.kelp.core.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack


fun Material.item(
    amount: Int = 1,
    builder: ItemStack.() -> Unit = {}
): CustomItem {

    return CustomItem(
        ItemStack(this, amount)
            .apply(builder)
    )
}


fun ItemStack.custom(): CustomItem {

    return CustomItem(
        this
    )
}