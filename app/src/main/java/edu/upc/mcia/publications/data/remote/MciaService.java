package edu.upc.mcia.publications.data.remote;

import java.util.List;

import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publisher;
import retrofit2.http.GET;
import rx.Observable;

public interface MciaService {

    @GET("authors")
    Observable<List<Author>> getAuthors();

    @GET("publishers")
    Observable<List<Publisher>> getPublishers();
}
