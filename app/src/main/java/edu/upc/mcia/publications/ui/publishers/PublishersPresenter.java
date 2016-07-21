package edu.upc.mcia.publications.ui.publishers;

import edu.upc.mcia.publications.data.DataManager;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;

public class PublishersPresenter extends BasePresenter<PublishersMvpView> {

    private DataManager mDataManager;

    private Subscription mSubscription;

    public PublishersPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void attachView(PublishersMvpView mvpView) {
        super.attachView(mvpView);

        loadPublishers();
    }

    private void loadPublishers() {
        checkViewAttached();
        mSubscription = mDataManager.getPublishers()
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
