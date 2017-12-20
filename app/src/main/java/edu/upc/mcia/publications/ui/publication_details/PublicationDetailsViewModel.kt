package edu.upc.mcia.publications.ui.publication_details

import android.arch.lifecycle.MutableLiveData
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.data.repository.PublicationRepository
import edu.upc.mcia.publications.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PublicationDetailsViewModel(val publicationId: String) : BaseViewModel() {

    private val publicationRepository = PublicationRepository

    val uiState = MutableLiveData<UiState>().apply {
        value = UiState()
    }

    init {
        publicationRepository.findById(publicationId)
                .map { uiState.value!!.copy(loading = false, publication = it) }
                .startWith(uiState.value!!.copy(loading = true, error = null))
                .onErrorReturn { uiState.value!!.copy(loading = false, error = it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { uiState.value = it }
    }

    data class UiState(
            val loading: Boolean = false,
            val error: Throwable? = null,
            val publication: Publication? = null) {
    }
}