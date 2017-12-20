package edu.upc.mcia.publications.ui.authors

import android.arch.lifecycle.MutableLiveData
import com.jakewharton.rxrelay2.PublishRelay
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.data.repository.AuthorRepository
import edu.upc.mcia.publications.ui.base.BaseViewModel
import edu.upc.mcia.publications.ui.utils.addTo
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AuthorsViewModel : BaseViewModel() {

    private val authorRepository = AuthorRepository

    val uiEvents: PublishRelay<UiEvent> = PublishRelay.create()
    val uiState = MutableLiveData<UiState>().apply {
        value = UiState()
    }

    private val queryChangeEventTransformer = ObservableTransformer<UiEvent.QueryChange, UiState> { upstream ->
        upstream.flatMap { uiEvent ->
            authorRepository.findAll()
                    .map { it.filter { it.name.contains(uiEvent.query, ignoreCase = true) } }
                    .map { uiState.value!!.copy(loading = false, authors = it) }
                    .startWith(uiState.value!!.copy(loading = true, error = null))
                    .onErrorReturn { uiState.value!!.copy(loading = false, error = Error(it, "Can't load authors", uiEvent)) }
                    .subscribeOn(Schedulers.io())
        }
    }

    init {
        uiEvents
                .doOnNext { Timber.d("Publications UiEvent : $it") }
                .publish { shared ->
                    shared.ofType(UiEvent.QueryChange::class.java)
                            .debounce(200, TimeUnit.MILLISECONDS)
                            .compose(queryChangeEventTransformer)
                }
                .doOnNext { Timber.d("Publications UiState: $it") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ uiState.value = it })
                .addTo(disposables)
    }

    data class UiState(
            val loading: Boolean = false,
            val error: Error? = null,
            val authors: List<Author> = emptyList()) {
    }

    class Error(val throwable: Throwable, val message: String, val trigger: UiEvent)

    sealed class UiEvent {
        class QueryChange(val query: String) : UiEvent()
    }
}
