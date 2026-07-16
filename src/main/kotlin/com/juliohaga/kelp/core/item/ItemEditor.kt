package com.juliohaga.kelp.core.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta


class ItemEditor(
    private val item: ItemStack
) {


    fun material(
        material: Material
    ) {

        item.type = material
    }


    fun name(
        name: String
    ) {

        editMeta {

            displayName(
                net.kyori.adventure.text.Component.text(name)
            )
        }
    }


    fun lore(
        lore: List<String>
    ) {

        editMeta {

            this.lore(
                lore.map {
                    net.kyori.adventure.text.Component.text(it)
                }
            )
        }
    }


    fun addEnchant(
        enchantment: Enchantment,
        level: Int
    ) {

        item.addUnsafeEnchantment(
            enchantment,
            level
        )
    }


    fun clearEnchants() {

        item.enchantments.keys.forEach {
            item.removeEnchantment(it)
        }
    }


    fun amount(
        amount: Int
    ) {

        item.amount = amount
    }


    fun unbreakable(
        value: Boolean
    ) {

        editMeta {
            isUnbreakable = value
        }
    }


    fun addFlag(
        flag: ItemFlag
    ) {

        editMeta {
            addItemFlags(flag)
        }
    }


    fun clearFlags() {

        editMeta {
            itemFlags.forEach {
                removeItemFlags(it)
            }
        }
    }


    private fun editMeta(
        block: ItemMeta.() -> Unit
    ) {

        val meta = item.itemMeta ?: return

        meta.block()

        item.itemMeta = meta
    }
}