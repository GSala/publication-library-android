package edu.upc.mcia.publications.ui.publications

import android.view.View
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.utils.toMonthAndYear

class PublicationItem(val publication: Publication,
                      val onPublicationClick: (Publication) -> Unit = {},
                      val onAuthorClick: (Author) -> Unit = {}) : BaseItem {

    val title = publication.title
    val subtitle = "${publication.publisher.acronym} - ${publication.publishDate.toMonthAndYear()}"
    val content = publication.summary

    private val author = publication.authors.first()
    val authorAvatar = author.photo
    val authorName = author.name
    val authorEmail = author.email

    fun onPublicationClick(view: View) {
        onPublicationClick(publication)
    }

    fun onAuthorClick(view: View) {
        onAuthorClick(publication.authors.first())
    }

    override val layoutResource: Int = R.layout.row_publication

    override fun isItemTheSame(obj: Any): Boolean =
            obj is PublicationItem && obj.publication.id == this.publication.id

    override fun areContentsTheSame(obj: Any): Boolean =
            obj is PublicationItem && obj.publication == this.publication

}