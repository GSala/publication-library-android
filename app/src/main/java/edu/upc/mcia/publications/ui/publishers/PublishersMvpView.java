package edu.upc.mcia.publications.ui.publishers;

import java.util.List;

import edu.upc.mcia.publications.data.model.Publisher;
import edu.upc.mcia.publications.ui.MvpView;

public interface PublishersMvpView extends MvpView {

    void showPublishers(List<Publisher> pubs);
}
