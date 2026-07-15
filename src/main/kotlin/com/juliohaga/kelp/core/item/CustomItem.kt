package com.juliohaga.kelp.core.item

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class CustomItem(
    material: Material,
    amount: Int = 1,
    name: Component? = null,
    lore: List<Component>? = null,
    builder: (ItemStack.() -> Unit)? = null
) {

    val item: ItemStack = ItemStack(material, amount).apply {
        itemMeta = itemMeta.apply {
            name?.let(::itemName)
            lore?.let(::lore)
        }

        builder?.invoke(this)
    }
}