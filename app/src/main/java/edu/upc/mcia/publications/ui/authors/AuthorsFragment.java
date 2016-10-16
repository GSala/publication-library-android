package edu.upc.mcia.publications.ui.authors;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.support.v4.view.RxMenuItemCompat;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;
import com.jakewharton.rxbinding.view.MenuItemActionViewEvent;

import java.util.List;

import edu.upc.mcia.publications.R;
import edu.upc.mcia.publications.data.model.Author;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class AuthorsFragment extends Fragment implements AuthorsMvpView {

    private AuthorsPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private AuthorsAdapter mAdapter;

    public AuthorsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new AuthorsPresenter();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authors, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AuthorsAdapter();
        mAdapter.setOnItemClickListener((v, author) -> Timber.d("Item clicked"));

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_authors, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        RxMenuItemCompat.actionViewEvents(searchItem)
                .filter(event -> event.kind().equals(MenuItemActionViewEvent.Kind.COLLAPSE))
                .subscribe(event -> mPresenter.onSearchQueryDismissed());

        RxSearchView.queryTextChangeEvents(searchView)
                .filter(SearchViewQueryTextEvent::isSubmitted)
                .map(query -> query.queryText().toString().trim())
                .subscribe(query -> mPresenter.onSearchQuerySubmitted(query));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void showAuthors(List<Author> authors) {
        Timber.d("Received authors");
        mAdapter.setData(authors);
    }

    @Override
    public void filterAuthors(String query) {
        mAdapter.getFilter().filter(query);
    }
}
