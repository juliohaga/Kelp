package com.juliohaga.kelp.core.item.modifier

import org.bukkit.inventory.ItemStack

fun interface ItemModifier {

    fun apply(item: ItemStack)

}