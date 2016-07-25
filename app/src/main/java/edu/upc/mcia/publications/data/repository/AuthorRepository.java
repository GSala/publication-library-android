package edu.upc.mcia.publications.data.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.remote.ApiManager;
import rx.Observable;

public class AuthorRepository {
    private static AuthorRepository INSTANCE = null;

    private final Map<String, Author> mData = new HashMap<>();

    private AuthorRepository() {
    }

    public static AuthorRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AuthorRepository();
        }
        return INSTANCE;
    }

    public Observable<List<Author>> findAll() {
        if (mData.isEmpty()) {
            return ApiManager.getRemoteApi().getAuthors()
                    .concatMap(Observable::from)
                    .doOnNext(a -> mData.put(a.getId(), a))
                    .toList();
        } else {
            return Observable.from(mData.values()).toList();
        }
    }

    public Observable<Author> findById(String id) {
        if (!mData.containsKey(id)) {
            return ApiManager.getRemoteApi().getAuthor(id)
                    .doOnNext(a -> mData.put(a.getId(), a));
        } else {
            return Observable.just(mData.get(id));
        }
    }

}
