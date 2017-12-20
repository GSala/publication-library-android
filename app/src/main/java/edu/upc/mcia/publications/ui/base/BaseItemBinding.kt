package edu.upc.mcia.publications.ui.base

import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.OnItemBind

class BaseItemBinding : OnItemBind<BaseItem> {

    override fun onItemBind(itemBinding: ItemBinding<*>, position: Int, item: BaseItem) {
        itemBinding.set(item.bindingId, item.layoutResource)
    }
}