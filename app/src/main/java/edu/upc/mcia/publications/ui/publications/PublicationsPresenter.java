package edu.upc.mcia.publications.ui.publications;

import edu.upc.mcia.publications.data.repository.PublicationQuery;
import edu.upc.mcia.publications.data.repository.PublicationRepository;
import edu.upc.mcia.publications.ui.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class PublicationsPresenter extends BasePresenter<PublicationsMvpView> {

    private final PublicationRepository mPublicationRepository;
    private final QueryManager mQueryManager;

    private Disposable mSubscription;
    private int totalPages;
    private int lastPageLoaded;

    public PublicationsPresenter() {
        mPublicationRepository = PublicationRepository.getInstance();
        mQueryManager = new QueryManager();

        mQueryManager.getObservable().subscribe(query -> {
            checkViewAttached();
            getMvpView().clearPublications();
            lastPageLoaded = -1;
            loadPublications(lastPageLoaded + 1, query);
        });

    }

    @Override
    public void attachView(PublicationsMvpView mvpView) {
        super.attachView(mvpView);

        loadPublications(0, mQueryManager.getQuery());
    }

    private void loadPublications(int pageNumber, PublicationQuery query) {
        checkViewAttached();
        getMvpView().showLoadingMoreIndicator(true);
        Timber.d("Loading publications. Page " + (pageNumber + 1) + "/" + totalPages);

        mSubscription = mPublicationRepository.search(pageNumber, query.getQuery())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        page -> {
                            getMvpView().showLoadingMoreIndicator(false);
                            Timber.d("Received publications");
                            getMvpView().addPublications(page.getContent());
                            totalPages = page.getMetadata().getTotalPages();
                            lastPageLoaded = page.getMetadata().getPage();
                        },
                        error -> {
                            getMvpView().showLoadingMoreIndicator(false);
                            Timber.e(error, error.getMessage());
                        });
    }

    public void onScroll(int firstVisibleItem, int totalItems) {
        if (firstVisibleItem >= totalItems - 2) {
            onScrollReachedBottom();
        }
    }

    private void onScrollReachedBottom() {
        if (mSubscription.isDisposed() && (lastPageLoaded < totalPages - 1)) {
            loadPublications(lastPageLoaded + 1, mQueryManager.getQuery());
        }
    }

    public void onRefreshRequested() {
        getMvpView().showLoadingIndicator(false);
    }

    public void onSearchQueryDismissed() {
        mQueryManager.changeQueryText("");
    }

    public void onSearchQuerySubmitted(String query) {
        mQueryManager.changeQueryText(query);
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isDisposed()) {
            mSubscription.dispose();
        }
    }
}
