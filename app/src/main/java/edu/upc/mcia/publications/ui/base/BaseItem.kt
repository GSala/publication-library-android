package edu.upc.mcia.publications.ui.base

import edu.upc.mcia.publications.BR

interface BaseItem {
    val bindingId: Int
        get() = BR.viewModel

    val layoutResource: Int
    fun isItemTheSame(obj: Any): Boolean = false
    fun areContentsTheSame(obj: Any): Boolean = false
}