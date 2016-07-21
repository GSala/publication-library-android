package edu.upc.mcia.publications.ui.authors;

import edu.upc.mcia.publications.data.remote.ApiManager;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AuthorsPresenter extends BasePresenter<AuthorsMvpView> {

    private Subscription mSubscription;

    @Override
    public void attachView(AuthorsMvpView mvpView) {
        super.attachView(mvpView);

        loadAuthors();
    }

    private void loadAuthors() {
        mSubscription = ApiManager.getMciaService().getAuthors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authors -> getMvpView().showAuthors(authors),
                        error -> Timber.e(error, error.getMessage()));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
