package edu.upc.mcia.publications.ui.publications;

import edu.upc.mcia.publications.data.repository.PublicationQuery;
import edu.upc.mcia.publications.data.repository.PublicationRepository;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PublicationsPresenter extends BasePresenter<PublicationsMvpView> {

    private final PublicationRepository mPublicationRepository;
    private final QueryManager mQueryManager;

    private Subscription mSubscription;
    private int totalPages;
    private int lastPageLoaded;
    private int totalPublications;
    private String textQuery = "";

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
        getMvpView().showLoadingIndicator(true);
        Timber.d("Loading publications. Page " + (pageNumber + 1) + "/" + totalPages);

        mSubscription = mPublicationRepository.search(pageNumber, query.getQuery())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> getMvpView().showLoadingIndicator(false))
                .subscribe(
                        page -> {
                            getMvpView().addPublications(page.getContent());
                            totalPages = page.getMetadata().getTotalPages();
                            totalPublications = (int) page.getMetadata().getTotalElements();
                            lastPageLoaded = page.getMetadata().getPage();
                        },
                        error -> Timber.e(error, error.getMessage()));
    }

    public void onScroll(int firstVisibleItem, int totalItems) {
        if (firstVisibleItem >= totalItems - 2) {
            onScrollReachedBottom();
        }
        getMvpView().setPageIndicator(firstVisibleItem + 1, totalPublications);
    }

    private void onScrollReachedBottom() {
        if (mSubscription.isUnsubscribed() && (lastPageLoaded < totalPages - 1)) {
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

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
