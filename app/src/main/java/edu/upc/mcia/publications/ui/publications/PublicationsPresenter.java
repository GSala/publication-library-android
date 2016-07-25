package edu.upc.mcia.publications.ui.publications;

import edu.upc.mcia.publications.data.repository.PublicationRepository;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class PublicationsPresenter extends BasePresenter<PublicationsMvpView> {

    private PublicationRepository mPublicationRepository;

    private Subscription mSubscription;
    private int totalPages;
    private int lastPageLoaded;

    public PublicationsPresenter() {
        mPublicationRepository = PublicationRepository.getInstance();
    }

    @Override
    public void attachView(PublicationsMvpView mvpView) {
        super.attachView(mvpView);

        loadPublications(0);
    }

    private void loadPublications(int pageNumber) {
        checkViewAttached();
        getMvpView().showLoadingIndicator(true);
        Timber.d("Loading publications. Page " + (pageNumber + 1) + "/" + totalPages);
        mSubscription = mPublicationRepository.search(pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> getMvpView().showLoadingIndicator(false))
                .subscribe(page -> {
                            getMvpView().addPublications(page.getContent());
                            totalPages = page.getMetadata().getTotalPages();
                            lastPageLoaded = pageNumber;
                        }
                        , error -> Timber.e(error, error.getMessage()));
    }

    public void onScrollReachedBottom() {
        if (lastPageLoaded < totalPages - 1) {
            loadPublications(lastPageLoaded + 1);
        }
    }

    public void onRefreshRequested() {
        getMvpView().showLoadingIndicator(false);
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
