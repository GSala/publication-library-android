package edu.upc.mcia.publications.ui.publications

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.support.v7.widget.scrollEvents
import edu.upc.mcia.publications.BR
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.databinding.FragmentPublicationsBinding
import edu.upc.mcia.publications.ui.base.BaseFragment
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.base.BaseItemBinding
import edu.upc.mcia.publications.ui.base.BaseItemDiffCallback
import edu.upc.mcia.publications.ui.common.ErrorItem
import edu.upc.mcia.publications.ui.common.LoadingItem
import edu.upc.mcia.publications.ui.publication_details.PublicationDetailsActivity
import edu.upc.mcia.publications.ui.utils.bindingProvider
import edu.upc.mcia.publications.ui.utils.viewModelProvider
import me.tatarka.bindingcollectionadapter2.OnItemBind
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import java.util.concurrent.TimeUnit

class PublicationsFragment : BaseFragment() {

    override val binding: FragmentPublicationsBinding
            by bindingProvider(R.layout.fragment_publications, BR.publicationsFragment to this)

    private val model: PublicationsViewModel by viewModelProvider { PublicationsViewModel() }

    val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }
    val itemBinding: OnItemBind<BaseItem> = BaseItemBinding()
    val items: DiffObservableList<BaseItem> = DiffObservableList(BaseItemDiffCallback())

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.invalidateOptionsMenu()
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)

        savedInstanceState?.let { layoutManager.onRestoreInstanceState(it.getParcelable(EXTRA_LAYOUT_MANAGER_STATE)) }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshLayout.setColorSchemeResources(R.color.colorAccent)
        val refreshObs = binding.refreshLayout.refreshes().share()

        refreshObs
                .startWith(Unit)
                .switchMap {
                    binding.recyclerView.scrollEvents()
                            .throttleLast(200, TimeUnit.MILLISECONDS)
                            .map { (layoutManager.findFirstVisibleItemPosition() + 5) / 10 }
                            .filter { it > 0 }
                            .distinct()
                }
                .map { PublicationsViewModel.UiEvent.PageNeeded(it) }
                .subscribe(model.uiEvents)

        refreshObs.map { PublicationsViewModel.UiEvent.Refreshed() }
                .subscribe(model.uiEvents)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.uiState.observe(this, Observer {
            it!!
            binding.refreshLayout.isRefreshing = it.loading

            val listItems = mutableListOf<BaseItem>()

            listItems.addAll(it.publications.map { PublicationItem(it, { showPublicationDetails(it.id) }) })

            if (it.loadingPage) listItems += LoadingItem()
            it.error?.let { listItems += ErrorItem(it.message, { model.uiEvents.accept(it.trigger) }) }
            items.update(listItems)
        })
    }

    override fun onStart() {
        super.onStart()

        model.uiEvents.accept(PublicationsViewModel.UiEvent.PageNeeded(0))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(EXTRA_LAYOUT_MANAGER_STATE, layoutManager.onSaveInstanceState())
    }

    private fun showPublicationDetails(publicationId: String) {
        startActivity(PublicationDetailsActivity.getIntent(context, publicationId))
    }

    companion object {
        private val EXTRA_LAYOUT_MANAGER_STATE = "extra_layout_manager_state"

    }
}