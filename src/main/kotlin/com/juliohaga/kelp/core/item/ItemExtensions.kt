package com.juliohaga.kelpToolbox.commands.change.item

import com.juliohaga.kelp.core.item.ItemEditor
import org.bukkit.inventory.ItemStack


fun ItemStack.edit(
    block: ItemEditor.() -> Unit
) {

    ItemEditor(this)
        .apply(block)
}