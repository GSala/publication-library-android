package edu.upc.mcia.publications.ui.publication_details

import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.ui.base.BaseItem

class PublicationDetailsAbstractItem(publication: Publication) : BaseItem {

    val abstract = publication.summary

    override val layoutResource: Int = R.layout.page_publication_details_abstract

    override fun isItemTheSame(obj: Any): Boolean = false

    override fun areContentsTheSame(obj: Any): Boolean = false
}