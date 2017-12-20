package edu.upc.mcia.publications.ui.publication_details

import android.databinding.ObservableArrayList
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.base.BaseItemBinding
import edu.upc.mcia.publications.ui.common.IconWithTextItem
import edu.upc.mcia.publications.ui.utils.toMonthAndYear

class PublicationDetailsInfoItem(publication: Publication) : BaseItem {

    private val publisher = publication.publisher.fullname
    private val date = publication.publishDate.toMonthAndYear()
    private val url = publication.url
    private val reference = publication.reference

    val itemBinding = BaseItemBinding()
    val items = ObservableArrayList<BaseItem>().apply {
        addAll(listOf(
                IconWithTextItem(R.drawable.ic_publisher_black_24dp, "Publisher", publisher),
                IconWithTextItem(R.drawable.ic_calendar_black_24dp, "Date", date),
                IconWithTextItem(R.drawable.ic_link_black_24dp, "URL", url),
                IconWithTextItem(R.drawable.ic_quote_black_24dp, "Reference", reference)
        ))
    }

    override val layoutResource: Int = R.layout.page_publication_details_info
}