class ItemEditor(
    private val item: ItemStack
) {

    fun modifier(
        modifier: ItemModifier
    ): ItemEditor {

        modifier.apply(item)

        return this
    }


    fun meta(
        modifier: ArmorMetaModifier
    ): ItemEditor {

        modifier.apply(item)

        return this
    }


    fun build(): CustomItem {

        return CustomItem(
            item.clone()
        )
    }
}