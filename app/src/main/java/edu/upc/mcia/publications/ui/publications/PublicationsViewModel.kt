package edu.upc.mcia.publications.ui.publications

import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.data.repository.PublicationRepository
import edu.upc.mcia.publications.ui.base.BaseViewModel
import edu.upc.mcia.publications.ui.utils.addTo
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PublicationsViewModel : BaseViewModel() {

    private val publicationRepository = PublicationRepository

    val uiEvents: PublishRelay<UiEvent> = PublishRelay.create()
    val uiState = MutableLiveData<UiState>().apply {
        value = UiState()
    }

    private val refreshEventTransformer = ObservableTransformer<UiEvent.Refreshed, UiState> { upstream ->
        upstream.flatMap { uiEvent ->
            publicationRepository.search(0, "")
                    .map { uiState.value!!.copy(loading = false, publications = it.content) }
                    .startWith(uiState.value!!.copy(loading = true, error = null))
                    .onErrorReturn { uiState.value!!.copy(loading = false, error = Error(it, "Can't load publications", uiEvent)) }
                    .subscribeOn(Schedulers.io())
        }
    }

    private val scrollEventTransformer = ObservableTransformer<UiEvent.PageNeeded, UiState> { upstream ->
        upstream
                .filter { it.index >= (uiState.value!!.publications.size / 10) }
                .flatMap { uiEvent ->
                    publicationRepository.search(uiEvent.index, "")
                            .map { uiState.value!!.copy(loadingPage = false, publications = uiState.value!!.publications + it.content) }
                            .startWith(uiState.value!!.copy(loadingPage = true, error = null))
                            .onErrorReturn { uiState.value!!.copy(loadingPage = false, error = Error(it, "Can't load publications", uiEvent)) }
                            .subscribeOn(Schedulers.io())
                }
    }

    init {
        uiEvents
                .doOnNext { Timber.d("Publications UiEvent : $it") }
                .publish { shared ->
                    Observable.merge(
                            shared.ofType(UiEvent.Refreshed::class.java).compose(refreshEventTransformer),
                            shared.ofType(UiEvent.PageNeeded::class.java).compose(scrollEventTransformer)
                    )
                }
                .doOnNext { Timber.d("Publications UiState: $it") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uiState.value = it })
                .addTo(disposables)
    }

    data class UiState(
            val loading: Boolean = false,
            val loadingPage: Boolean = false,
            val error: Error? = null,
            val publications: List<Publication> = emptyList()) {
        override fun toString(): String {
            return "loading = $loading; loadingPage = $loadingPage; publicationSize = ${publications.size}; error = $error"
        }
    }

    class Error(val throwable: Throwable, val message: String, val trigger: UiEvent)

    sealed class UiEvent {
        class Refreshed : UiEvent()
        class PageNeeded(val index: Int) : UiEvent()
    }
}