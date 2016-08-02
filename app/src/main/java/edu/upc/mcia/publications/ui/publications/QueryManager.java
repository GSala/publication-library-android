package edu.upc.mcia.publications.ui.publications;

import edu.upc.mcia.publications.data.repository.PublicationQuery;
import rx.Observable;
import rx.subjects.PublishSubject;

public class QueryManager {

    private final PublishSubject<PublicationQuery> mSubject = PublishSubject.create();

    private final PublicationQuery mQuery = new PublicationQuery();

    public Observable<PublicationQuery> getObservable() {
        return mSubject.asObservable();
    }

    public PublicationQuery getQuery() {
        return mQuery;
    }

    public void changeQueryText(String query) {
        if (!query.equals(mQuery.getQuery())) {
            mQuery.setQuery(query);
            mSubject.onNext(mQuery);
        }
    }


}
