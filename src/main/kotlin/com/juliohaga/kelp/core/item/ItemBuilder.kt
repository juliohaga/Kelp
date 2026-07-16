package com.juliohaga.kelpToolbox.commands.change.item

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta


class ItemBuilder(
    private val item: ItemStack
) {

    private val miniMessage = MiniMessage.miniMessage()


    fun name(
        value: String
    ) {

        editMeta {
            displayName(
                miniMessage.deserialize(value)
            )
        }
    }


    fun lore(
        vararg lines: String
    ) {

        editMeta {

            lore(
                lines.map {
                    miniMessage.deserialize(it)
                }
            )
        }
    }


    fun lore(
        lines: List<String>
    ) {

        editMeta {
            lore(
                lines.map {
                    miniMessage.deserialize(it)
                }
            )
        }
    }


    fun enchant(
        enchantment: Enchantment,
        level: Int
    ) {

        item.addUnsafeEnchantment(
            enchantment,
            level
        )
    }


    fun unbreakable(
        value: Boolean = true
    ) {

        editMeta {
            isUnbreakable = value
        }
    }


    fun flag(
        flag: ItemFlag
    ) {

        editMeta {
            addItemFlags(flag)
        }
    }


    fun amount(
        amount: Int
    ) {
        item.amount = amount
    }


    private fun editMeta(
        block: ItemMeta.() -> Unit
    ) {

        val meta = item.itemMeta ?: return

        meta.block()

        item.itemMeta = meta
    }


    fun build(): ItemStack {
        return item
    }
}