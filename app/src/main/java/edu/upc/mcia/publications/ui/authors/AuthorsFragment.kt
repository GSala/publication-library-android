package edu.upc.mcia.publications.ui.authors

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.jakewharton.rxbinding.support.v4.view.RxMenuItemCompat
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent
import edu.upc.mcia.publications.BR
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.databinding.FragmentAuthorsBinding
import edu.upc.mcia.publications.ui.BaseFragment
import edu.upc.mcia.publications.ui.bindingProvider
import edu.upc.mcia.publications.ui.viewModelProvider
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList

/**
 * A simple [Fragment] subclass.
 */
class AuthorsFragment : BaseFragment() {

    override val binding: FragmentAuthorsBinding by bindingProvider(R.layout.fragment_authors, BR.authorsFragment to this)
    private val model by viewModelProvider { AuthorsViewModel() }

    val itemBinding: ItemBinding<Author> = ItemBinding.of<Author>(BR.author, R.layout.row_author)
    val items: DiffObservableList<Author> = DiffObservableList(object : DiffObservableList.Callback<Author> {
        override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.authors.observe(this, Observer { showAuthors(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_authors, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView

        if (model.authorQuery.isNotBlank()) {
            searchItem.expandActionView()
            searchView.setQuery(model.authorQuery, false)
        }

        RxMenuItemCompat.actionViewEvents(searchItem)
                .filter { event -> event.kind() == MenuItemActionViewEvent.Kind.COLLAPSE }
                .subscribe { model.authorQuery = "" }

        RxSearchView.queryTextChangeEvents(searchView)
                .filter({ it.isSubmitted })
                .map { it.queryText().toString().trim { it <= ' ' } }
                .subscribe { model.authorQuery = it }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showAuthors(authors: List<Author>?) {
        items.update(authors.orEmpty())
    }
}
