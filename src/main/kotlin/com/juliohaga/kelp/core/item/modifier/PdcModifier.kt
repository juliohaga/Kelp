package com.juliohaga.kelp.core.item.modifier

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


class PdcModifier<T : Any, Z : Any>(
    private val key: NamespacedKey,
    private val type: PersistentDataType<T, Z>,
    private val value: Z

) : ItemModifier {


    override fun apply(item: ItemStack) {

        item.editPersistentDataContainer {

            it.set(
                key,
                type,
                value
            )

        }

    }
}