package edu.upc.mcia.publications.ui.authors;


import java.util.List;

import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.ui.MvpView;

public interface AuthorsMvpView extends MvpView {

    void showAuthors(List<Author> authors);
}
