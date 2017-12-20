package edu.upc.mcia.publications.ui.authors

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.queryTextChangeEvents
import edu.upc.mcia.publications.BR
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.databinding.FragmentAuthorsBinding
import edu.upc.mcia.publications.ui.base.BaseFragment
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.base.BaseItemBinding
import edu.upc.mcia.publications.ui.base.BaseItemDiffCallback
import edu.upc.mcia.publications.ui.common.ErrorItem
import edu.upc.mcia.publications.ui.common.LoadingItem
import edu.upc.mcia.publications.ui.utils.bindingProvider
import edu.upc.mcia.publications.ui.utils.viewModelProvider
import me.tatarka.bindingcollectionadapter2.OnItemBind
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

/**
 * A simple [Fragment] subclass.
 */
class AuthorsFragment : BaseFragment() {

    override val binding: FragmentAuthorsBinding by bindingProvider(R.layout.fragment_authors, BR.authorsFragment to this)
    private val model by viewModelProvider { AuthorsViewModel() }

    val itemBinding: OnItemBind<BaseItem> = BaseItemBinding()
    val items: DiffObservableList<BaseItem> = DiffObservableList(BaseItemDiffCallback())

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.queryTextChangeEvents()
                .map { it.queryText().toString().trim { it <= ' ' } }
                .startWith("")
                .subscribe { model.uiEvents.accept(AuthorsViewModel.UiEvent.QueryChange(it)) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.uiState.observe(this, Observer {
            if (it!!.loading) items.update(listOf(LoadingItem()))
            if (it.error != null) items.update(listOf(ErrorItem(it.error.message, { model.uiEvents.accept(it.error.trigger) })))
            showAuthors(it.authors)
        })
    }

    private fun showAuthors(authors: List<Author>) {
        val listItems = mutableListOf<BaseItem>()

        listItems += authors
                .filter { it.status == "member" }
                .map { AuthorItem(it) }
                .sortedBy { it.sortingName }
                .also { if (it.isNotEmpty()) listItems += AuthorHeaderItem("Members") }

        listItems += authors
                .filter { it.status == "external" }
                .map { AuthorItem(author = it, showAvatar = false) }
                .sortedBy { it.sortingName }
                .also { if (it.isNotEmpty()) listItems += AuthorHeaderItem("External") }

        if (listItems.isEmpty()) listItems += AuthorEmptyItem()

        items.update(listItems)
        binding.recyclerView.scrollToPosition(0)
    }
}
