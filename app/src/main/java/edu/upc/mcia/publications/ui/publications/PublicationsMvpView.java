package edu.upc.mcia.publications.ui.publications;

import java.util.List;

import edu.upc.mcia.publications.data.model.Publication;
import edu.upc.mcia.publications.ui.MvpView;

public interface PublicationsMvpView extends MvpView {

    void addPublications(List<Publication> pubs);

    void clearPublications();

    void showLoadingIndicator(boolean show);

    void showLoadingMoreIndicator(boolean show);
}
