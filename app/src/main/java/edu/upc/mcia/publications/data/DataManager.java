package edu.upc.mcia.publications.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.upc.mcia.publications.data.local.AuthorsLocalDataSource;
import edu.upc.mcia.publications.data.local.PublishersLocalDataSource;
import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publisher;
import edu.upc.mcia.publications.data.remote.ApiManager;
import jonathanfinerty.once.Once;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DataManager {

    private static final DataManager INSTANCE = null;

    private static final String AUTHORS_REFRESH_KEY = "authors_refresh";
    private static final int AUTHORS_REFRESH_RATE_MINUTES = 180;
    private static final String PUBLISHERS_REFRESH_KEY = "publishers_refresh";
    private static final int PUBLISHERS_REFRESH_RATE_MINUTES = 180;

    private AuthorsLocalDataSource mAuthorsLocalSource;
    private PublishersLocalDataSource mPublishersLocalSource;

    private DataManager() {
        mAuthorsLocalSource = AuthorsLocalDataSource.getInstance();
        mPublishersLocalSource = PublishersLocalDataSource.getInstance();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            return new DataManager();
        }
        return INSTANCE;
    }

    public Observable<List<Author>> getAuthors() {
        if (!Once.beenDone(TimeUnit.MINUTES, AUTHORS_REFRESH_RATE_MINUTES, AUTHORS_REFRESH_KEY)) {
            refreshAuthorsData();
        }
        return mAuthorsLocalSource.getAuthors();
    }

    public Observable<List<Publisher>> getPublishers() {
        if (!Once.beenDone(TimeUnit.MINUTES, PUBLISHERS_REFRESH_RATE_MINUTES, PUBLISHERS_REFRESH_KEY)) {
            refreshPublishersData();
        }
        return mPublishersLocalSource.getPublishers();
    }

    private void refreshAuthorsData() {
        ApiManager.getMciaService().getAuthors()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(authors -> {
                            mAuthorsLocalSource.saveAuthors(authors);
                            Once.markDone(AUTHORS_REFRESH_KEY);
                        },
                        error -> Timber.e(error, error.getMessage()));
    }

    private void refreshPublishersData() {
        ApiManager.getMciaService().getPublishers()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(pubs -> {
                            mPublishersLocalSource.savePublishers(pubs);
                            Once.markDone(PUBLISHERS_REFRESH_KEY);
                        },
                        error -> Timber.e(error, error.getMessage()));
    }


}
