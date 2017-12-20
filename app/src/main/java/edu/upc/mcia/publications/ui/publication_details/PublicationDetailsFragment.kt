package edu.upc.mcia.publications.ui.publication_details

import android.arch.lifecycle.Observer
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.upc.mcia.publications.BR
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.databinding.FragmentPublicationDetailsBinding
import edu.upc.mcia.publications.ui.base.BaseFragment
import edu.upc.mcia.publications.ui.base.BaseItem
import edu.upc.mcia.publications.ui.base.BaseItemBinding
import edu.upc.mcia.publications.ui.utils.bindingProvider
import edu.upc.mcia.publications.ui.utils.viewModelProvider
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter

class PublicationDetailsFragment : BaseFragment() {

    override val binding: FragmentPublicationDetailsBinding by bindingProvider(R.layout.fragment_publication_details, BR.fragment to this)

    private val model: PublicationDetailsViewModel by viewModelProvider { PublicationDetailsViewModel(unpackPublicationId(arguments)) }

    val title = ObservableField<String>()

    val items = ObservableArrayList<BaseItem>()
    val itemBinding = BaseItemBinding()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.executePendingBindings()
        (binding.viewPager.adapter as? BindingViewPagerAdapter<BaseItem>)?.setPageTitles({ position, item ->
            when (item) {
                is PublicationDetailsAbstractItem -> "Abstract"
                is PublicationDetailsInfoItem -> "Info"
                is PublicationDetailsAuthorsItem -> "Authors"
                else -> ""
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.uiState.observe(this, Observer {
            it!!
            it.publication?.let {
                title.set(it.title)
                items.addAll(listOf(
                        PublicationDetailsAbstractItem(it),
                        PublicationDetailsInfoItem(it),
                        PublicationDetailsAuthorsItem(it)
                ))
            }

        })
    }


    fun onUpNavigationClick(view: View) {
        activity.onBackPressed()
    }

    companion object {

        private const val EXTRA_PUBLICATION_ID = "extra_publication_id"

        private fun unpackPublicationId(bundle: Bundle): String = bundle.getString(EXTRA_PUBLICATION_ID)

        fun newInstance(publicationId: String): PublicationDetailsFragment {
            val bundle = Bundle().apply { putString(EXTRA_PUBLICATION_ID, publicationId) }
            return PublicationDetailsFragment().apply { arguments = bundle }
        }
    }
}