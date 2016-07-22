package edu.upc.mcia.publications.ui.publications;

import java.util.List;

import edu.upc.mcia.publications.data.model.Publication;
import edu.upc.mcia.publications.ui.MvpView;

public interface PublicationsMvpView extends MvpView {

    void showPublications(List<Publication> pubs);
}
