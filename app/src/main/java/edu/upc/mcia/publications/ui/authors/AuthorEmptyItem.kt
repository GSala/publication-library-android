package edu.upc.mcia.publications.ui.authors

import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.ui.base.BaseItem

class AuthorEmptyItem : BaseItem {

    val message = "Can't find any results"

    override val layoutResource: Int
        get() = R.layout.row_author_empty

    override fun isItemTheSame(obj: Any): Boolean {
        return obj is AuthorEmptyItem
    }

    override fun areContentsTheSame(obj: Any): Boolean {
        return true
    }
}