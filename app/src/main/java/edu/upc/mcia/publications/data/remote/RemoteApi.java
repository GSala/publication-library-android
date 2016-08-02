package edu.upc.mcia.publications.data.remote;

import java.util.List;

import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publisher;
import edu.upc.mcia.publications.data.remote.dto.Page;
import edu.upc.mcia.publications.data.remote.dto.PublicationDto;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RemoteApi {

    @GET("authors")
    Observable<List<Author>> getAuthors();

    @GET("authors/{id}")
    Observable<Author> getAuthor(@Path("id") String id);

    @GET("publishers")
    Observable<List<Publisher>> getPublishers();

    @GET("publishers/{id}")
    Observable<Publisher> getPublisher(@Path("id") String id);

    @GET("publications")
    Observable<Page<PublicationDto>> getPublications(
            @Query("page") int pageNumber,
            @Query("q") String query);

}
