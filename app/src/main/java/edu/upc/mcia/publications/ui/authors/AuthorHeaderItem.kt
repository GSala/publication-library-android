package edu.upc.mcia.publications.ui.authors

import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.ui.base.BaseItem

class AuthorHeaderItem(val header: String) : BaseItem {

    override val layoutResource: Int
        get() = R.layout.row_author_header

    override fun isItemTheSame(obj: Any): Boolean {
        return obj is AuthorHeaderItem && obj.header == this.header
    }

    override fun areContentsTheSame(obj: Any): Boolean {
        return true
    }
}