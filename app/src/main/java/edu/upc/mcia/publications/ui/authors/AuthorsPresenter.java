package edu.upc.mcia.publications.ui.authors;

import edu.upc.mcia.publications.data.DataManager;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;

public class AuthorsPresenter extends BasePresenter<AuthorsMvpView> {

    private Subscription mSubscription;
    private DataManager mDataManager;

    public AuthorsPresenter() {
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void attachView(AuthorsMvpView mvpView) {
        super.attachView(mvpView);

        loadAuthors();
    }

    private void loadAuthors() {
        checkViewAttached();
        mDataManager.getAuthors().subscribe(authors -> getMvpView().showAuthors(authors));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
