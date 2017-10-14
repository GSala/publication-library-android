package edu.upc.mcia.publications.ui.authors

import android.arch.lifecycle.MutableLiveData
import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.data.repository.AuthorRepository
import edu.upc.mcia.publications.ui.BaseViewModel
import edu.upc.mcia.publications.ui.addTo
import edu.upc.mcia.publications.ui.observableValue
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AuthorsViewModel : BaseViewModel() {

    private val authorRepository = AuthorRepository.getInstance()
    private var authorList by observableValue(emptyList<Author>()) { onDataChange() }
    var authorQuery by observableValue("") { onDataChange() }

    val authors = MutableLiveData<List<Author>>()

    init {
        authorRepository.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ authorList = it }, { Timber.e(it) })
                .addTo(disposables)
    }

    private fun onDataChange() {
        authors.value = authorList.filter { it.name.contains(authorQuery, true) }
    }
}
