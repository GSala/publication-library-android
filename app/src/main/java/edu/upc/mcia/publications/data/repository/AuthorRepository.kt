package edu.upc.mcia.publications.data.repository

import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.data.remote.ApiManager
import io.reactivex.Observable
import java.util.*

object AuthorRepository {

    private val mData = HashMap<String, Author>()

    fun findAll(): Observable<List<Author>> {
        return if (mData.isEmpty()) {
            ApiManager.remoteApi.authors
                    .concatMap<Author>({ Observable.fromIterable(it) })
                    .doOnNext { a -> mData.put(a.id, a) }
                    .toList().toObservable()
        } else {
            Observable.fromIterable(mData.values).toList().toObservable()
        }
    }

    fun findById(id: String): Observable<Author> {
        return if (!mData.containsKey(id)) {
            ApiManager.remoteApi.getAuthor(id)
                    .doOnNext { a -> mData.put(a.id, a) }
        } else {
            Observable.just(mData[id])
        }
    }

}
