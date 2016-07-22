package edu.upc.mcia.publications.ui.publications;

import edu.upc.mcia.publications.data.DataManager;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;

public class PublicationsPresenter extends BasePresenter<PublicationsMvpView> {

    private DataManager mDataManager;

    private Subscription mSubscription;

    public PublicationsPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void attachView(PublicationsMvpView mvpView) {
        super.attachView(mvpView);

        loadPublications();
    }

    private void loadPublications() {
        checkViewAttached();
        mSubscription = mDataManager.getPublications()
                .subscribe(pubs -> getMvpView().showPublications(pubs));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
