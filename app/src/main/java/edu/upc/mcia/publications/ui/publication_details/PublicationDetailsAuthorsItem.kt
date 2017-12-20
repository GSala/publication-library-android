package edu.upc.mcia.publications.ui.publication_details

import android.databinding.ObservableArrayList
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.ui.authors.AuthorItem
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.base.BaseItemBinding

class PublicationDetailsAuthorsItem(publication: Publication) : BaseItem {

    val items = ObservableArrayList<BaseItem>().apply {
        addAll(publication.authors.map { AuthorItem(it) })
    }
    val itemBinding = BaseItemBinding()

    override val layoutResource: Int = R.layout.page_publication_details_authors
    override fun isItemTheSame(obj: Any): Boolean = false
    override fun areContentsTheSame(obj: Any): Boolean = false
}