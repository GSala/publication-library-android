package edu.upc.mcia.publications.ui.authors;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_authors, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AuthorsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((v, author) -> Timber.d("Item clicked"));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
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
}
