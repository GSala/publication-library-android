package edu.upc.mcia.publications.ui.common

import android.view.View
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.ui.base.BaseItem

class ErrorItem(val message: String,
                val onRetryClick: () -> Unit = {}) : BaseItem {

    fun onRetryClick(view: View) {
        onRetryClick()
    }

    override val layoutResource: Int = R.layout.row_error

    override fun isItemTheSame(obj: Any): Boolean {
        return obj is ErrorItem && obj.message == this.message
    }

    override fun areContentsTheSame(obj: Any): Boolean {
        return true
    }
}