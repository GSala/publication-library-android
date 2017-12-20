package edu.upc.mcia.publications.ui.authors

import android.view.View
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.ui.base.BaseItem

class AuthorItem(private val author: Author,
                 private val showAvatar: Boolean = true,
                 val onAuthorClick: (Author) -> Unit = {}) : BaseItem {

    val authorName: String
    val sortingName: String

    val authorAvatar = author.photo

    init {
        val parts = author.name.split(' ').filter { it.length > 1 };
        val sortIdx = if (parts.size < 4) {
            (parts.size - 1).coerceAtMost(1)
        } else {
            2
        }
        sortingName = parts.subList(sortIdx, parts.size).joinToString(separator = " ");
        authorName = parts.subList(sortIdx, parts.size).joinToString(separator = " ") +
                ", " + parts.subList(0, sortIdx).joinToString(separator = " ");
    }

    fun onAuthorClick(vew: View) {
        onAuthorClick(author)
    }

    override val layoutResource: Int
        get() {
            return if (showAvatar) {
                R.layout.row_author
            } else {
                R.layout.row_author_basic
            }
        }

    override fun isItemTheSame(obj: Any): Boolean =
            obj is AuthorItem && obj.author.id == this.author.id

    override fun areContentsTheSame(obj: Any): Boolean =
            obj is AuthorItem && obj.author == this.author
}