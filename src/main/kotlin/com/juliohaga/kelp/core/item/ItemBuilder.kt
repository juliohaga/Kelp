package com.juliohaga.kelp.core.item

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class ItemBuilder(
    material: Material,
    amount: Int = 1
) {

    private val item = ItemStack(material, amount)


    fun name(name: Component): ItemBuilder {
        item.editMeta {
            it.itemName(name)
        }

        return this
    }


    fun name(name: String): ItemBuilder {
        return name(Component.text(name))
    }


    fun lore(lore: List<Component>): ItemBuilder {
        item.editMeta {
            it.lore(lore)
        }

        return this
    }


    fun lore(vararg lore: Component): ItemBuilder {
        return lore(lore.toList())
    }


    fun amount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }


    fun enchant(
        enchantment: Enchantment,
        level: Int,
        unsafe: Boolean = false
    ): ItemBuilder {

        if (unsafe) {
            item.addUnsafeEnchantment(
                enchantment,
                level
            )
        } else {
            item.addEnchantment(
                enchantment,
                level
            )
        }

        return this
    }


    fun enchantments(
        enchantments: Map<Enchantment, Int>,
        unsafe: Boolean = false
    ): ItemBuilder {

        enchantments.forEach { (enchant, level) ->

            enchant(
                enchant,
                level,
                unsafe
            )
        }

        return this
    }


    fun clearEnchantments(): ItemBuilder {

        item.removeEnchantments()

        return this
    }


    fun flags(
        vararg flags: ItemFlag
    ): ItemBuilder {

        item.addItemFlags(*flags)

        return this
    }


    fun removeFlags(
        vararg flags: ItemFlag
    ): ItemBuilder {

        item.removeItemFlags(*flags)

        return this
    }


    fun unbreakable(
        value: Boolean = true
    ): ItemBuilder {

        item.editMeta {
            it.isUnbreakable = value
        }

        return this
    }


    fun customModelData(
        value: Int
    ): ItemBuilder {

        item.editMeta {
            it.setCustomModelData(value)
        }

        return this
    }


    fun glow(
        value: Boolean = true
    ): ItemBuilder {

        item.editMeta {
            it.setEnchantmentGlintOverride(value)
        }

        return this
    }


    fun pdc(
        key: NamespacedKey,
        value: String
    ): ItemBuilder {

        item.editPersistentDataContainer {

            it.set(
                key,
                PersistentDataType.STRING,
                value
            )
        }

        return this
    }


    /**
     * Escape hatch.
     *
     * Permite acessar qualquer API futura do Paper/Bukkit
     */
    fun edit(
        block: ItemStack.() -> Unit
    ): ItemBuilder {

        item.apply(block)

        return this
    }


    fun build(): CustomItem {

        return CustomItem(
            item.clone()
        )
    }
}