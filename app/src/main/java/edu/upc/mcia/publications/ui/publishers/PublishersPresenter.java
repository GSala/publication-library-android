package edu.upc.mcia.publications.ui.publishers;

import edu.upc.mcia.publications.data.repository.PublisherRepository;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PublishersPresenter extends BasePresenter<PublishersMvpView> {

    private PublisherRepository mPublisherRepository;

    private Subscription mSubscription;

    public PublishersPresenter() {
        mPublisherRepository = PublisherRepository.getInstance();
    }

    @Override
    public void attachView(PublishersMvpView mvpView) {
        super.attachView(mvpView);

        loadPublishers();
    }

    private void loadPublishers() {
        checkViewAttached();
        mSubscription = mPublisherRepository.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pubs -> getMvpView().showPublishers(pubs));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
