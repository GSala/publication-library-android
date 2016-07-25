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

    public PublicationsPresenter() {
        mPublicationRepository = PublicationRepository.getInstance();
    }

    @Override
    public void attachView(PublicationsMvpView mvpView) {
        super.attachView(mvpView);

        loadPublications();
    }

    private void loadPublications() {
        checkViewAttached();
        mSubscription = mPublicationRepository.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(page -> getMvpView().showPublications(page.getContent())
                        , error -> Timber.e(error, error.getMessage()));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
