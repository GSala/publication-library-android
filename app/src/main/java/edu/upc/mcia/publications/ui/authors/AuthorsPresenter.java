package edu.upc.mcia.publications.ui.authors;

import edu.upc.mcia.publications.data.repository.AuthorRepository;
import edu.upc.mcia.publications.ui.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AuthorsPresenter extends BasePresenter<AuthorsMvpView> {

    private Subscription mSubscription;
    private AuthorRepository mAuthorRepository;

    public AuthorsPresenter() {
        mAuthorRepository = AuthorRepository.getInstance();
    }

    @Override
    public void attachView(AuthorsMvpView mvpView) {
        super.attachView(mvpView);

        loadAuthors();
    }

    private void loadAuthors() {
        checkViewAttached();
        mSubscription = mAuthorRepository.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authors -> getMvpView().showAuthors(authors));
    }

    @Override
    public void detachView() {
        super.detachView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
