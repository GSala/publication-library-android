package edu.upc.mcia.publications.ui.publications;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Publication;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicationsFragment extends Fragment implements PublicationsMvpView {

    private PublicationsPresenter mPresenter;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PublicationsAdapter mAdapter;

    public PublicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new PublicationsPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publications, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PublicationsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnPublicationClickListener((v, pub) -> Timber.d("Pub clicked"));
        mAdapter.setOnAuthorClickListener((v, author) -> Timber.d("Author clicked"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this);

        RxSwipeRefreshLayout.refreshes(mRefreshLayout)
                .subscribe(Void -> mPresenter.onRefreshRequested());

        RxRecyclerView.scrollEvents(mRecyclerView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter(e -> !mRefreshLayout.isRefreshing())
                .subscribe(event -> {
                    if (event.dy() > 0) //check for scroll down
                    {
                        int visibleItemCount = mLayoutManager.getChildCount();
                        int totalItemCount = mLayoutManager.getItemCount();
                        int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mPresenter.onScrollReachedBottom();
                        }

                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void addPublications(List<Publication> pubs) {
        mAdapter.addData(pubs);
    }

    @Override
    public void clearPublications(List<Publication> pubs) {
        Timber.d("Publications received: " + pubs.size());
        mAdapter.clearData(pubs);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        Observable.just(show)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(RxSwipeRefreshLayout.refreshing(mRefreshLayout));
    }

}
