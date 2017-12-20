package edu.upc.mcia.publications.ui.base

import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

class BaseItemDiffCallback : DiffObservableList.Callback<BaseItem> {
    override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean =
            oldItem.isItemTheSame(newItem)

    override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean =
            oldItem.areContentsTheSame(newItem)
}