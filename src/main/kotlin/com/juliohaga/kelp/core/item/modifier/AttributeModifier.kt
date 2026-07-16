package com.juliohaga.kelp.core.item.modifier

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack

class AttributeModifier(
    private val attribute: Attribute,
    private val modifier: AttributeModifier
) : ItemModifier {


    override fun apply(item: ItemStack) {

        item.editMeta {

            it.addAttributeModifier(
                attribute,
                modifier
            )

        }

    }
}