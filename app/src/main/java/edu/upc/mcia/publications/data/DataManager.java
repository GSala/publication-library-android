package edu.upc.mcia.publications.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.upc.mcia.publications.data.local.AuthorsLocalDataSource;
import edu.upc.mcia.publications.data.local.PublicationsLocalDataSource;
import edu.upc.mcia.publications.data.local.PublishersLocalDataSource;
import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publication;
import edu.upc.mcia.publications.data.model.Publisher;
import edu.upc.mcia.publications.data.remote.ApiManager;
import jonathanfinerty.once.Once;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class DataManager {

    private static final DataManager INSTANCE = null;

    private static final String AUTHORS_REFRESH_KEY = "authors_refresh";
    private static final int AUTHORS_REFRESH_RATE_MINUTES = 180;
    private static final String PUBLISHERS_REFRESH_KEY = "publishers_refresh";
    private static final int PUBLISHERS_REFRESH_RATE_MINUTES = 180;
    private static final String PUBLICATIONS_REFRESH_KEY = "publications_refresh";
    private static final int PUBLICATIONS_REFRESH_RATE_MINUTES = 30;

    private AuthorsLocalDataSource mAuthorsLocalSource;
    private PublishersLocalDataSource mPublishersLocalSource;
    private PublicationsLocalDataSource mPublicationsLocalSource;

    private DataManager() {
        mAuthorsLocalSource = AuthorsLocalDataSource.getInstance();
        mPublishersLocalSource = PublishersLocalDataSource.getInstance();
        mPublicationsLocalSource = PublicationsLocalDataSource.getInstance();
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

    public Observable<List<Publication>> getPublications() {
        /*if (!Once.beenDone(TimeUnit.MINUTES, PUBLICATIONS_REFRESH_RATE_MINUTES, PUBLICATIONS_REFRESH_KEY)) {
            refreshPublicationsData();
        }
        return mPublicationsLocalSource.getPublications();*/
        return ApiManager.getMciaService().getPublications()
                .map(page -> page.content())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void refreshAuthorsData() {
        ApiManager.getMciaService().getAuthors()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(authors -> {
                            mAuthorsLocalSource.saveAuthors(authors);
                            Timber.d("Refreshed local author data");
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
                            Timber.d("Refreshed local publisher data");
                            Once.markDone(PUBLISHERS_REFRESH_KEY);
                        },
                        error -> Timber.e(error, error.getMessage()));
    }

    private void refreshPublicationsData() {
        ApiManager.getMciaService().getPublications()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(page -> {
                    mPublicationsLocalSource.savePublications(page.content());
                    Timber.d("Refreshed local publication data");
                    Once.markDone(PUBLICATIONS_REFRESH_KEY);
                }, error -> Timber.e(error, error.getMessage()));
    }


}
