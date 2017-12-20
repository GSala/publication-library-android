package edu.upc.mcia.publications.ui.common

import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.ui.base.BaseItem

class LoadingItem : BaseItem {

    override val layoutResource: Int = R.layout.row_loading

    override fun isItemTheSame(obj: Any): Boolean {
        return obj is LoadingItem
    }

    override fun areContentsTheSame(obj: Any): Boolean {
        return true
    }
}