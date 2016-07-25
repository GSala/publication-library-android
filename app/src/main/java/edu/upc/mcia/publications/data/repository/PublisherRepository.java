package edu.upc.mcia.publications.data.repository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upc.mcia.publications.data.model.Publisher;
import edu.upc.mcia.publications.data.remote.ApiManager;
import rx.Observable;

public class PublisherRepository {

    private static PublisherRepository INSTANCE = null;

    private final Map<String, Publisher> mData = new HashMap<>();

    private PublisherRepository() {
    }

    public static PublisherRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PublisherRepository();
        }
        return INSTANCE;
    }

    public Observable<List<Publisher>> findAll() {
        if (mData.isEmpty()) {
            return ApiManager.getRemoteApi().getPublishers()
                    .concatMap(Observable::from)
                    .doOnNext(p -> mData.put(p.getId(), p))
                    .toList();
        } else {
            return Observable.from(mData.values()).toList();
        }
    }

    public Observable<Publisher> findById(String id) {
        if (!mData.containsKey(id)) {
            return ApiManager.getRemoteApi().getPublisher(id)
                    .doOnNext(p -> mData.put(p.getId(), p));
        } else {
            return Observable.just(mData.get(id));
        }
    }

}
