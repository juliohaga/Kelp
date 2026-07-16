package com.juliohaga.kelpToolbox.commands.change.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomItem(
    material: Material,
    amount: Int = 1,
    builder: ItemBuilder.() -> Unit = {}
) {

    val item: ItemStack = ItemStack(material, amount).apply {
        ItemBuilder(this).apply(builder).build()
    }
}