package edu.upc.mcia.publications.data.repository;

import java.util.List;

import edu.upc.mcia.publications.data.model.Author;
import edu.upc.mcia.publications.data.model.Publication;
import edu.upc.mcia.publications.data.remote.ApiManager;
import edu.upc.mcia.publications.data.remote.dto.Page;
import edu.upc.mcia.publications.data.remote.dto.PublicationDto;
import rx.Observable;

public class PublicationRepository {

    private static PublicationRepository INSTANCE = null;

    private final AuthorRepository mAuthorRepository;
    private final PublisherRepository mPublisherRepository;

    private PublicationRepository() {
        mAuthorRepository = AuthorRepository.getInstance();
        mPublisherRepository = PublisherRepository.getInstance();
    }

    public static PublicationRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PublicationRepository();
        }
        return INSTANCE;
    }

    public Observable<Page<Publication>> search(int pageNumber) {
        return ApiManager.getRemoteApi().getPublications(pageNumber)
                .flatMap(this::fillPublication);
    }

    private Observable<Page<Publication>> fillPublication(Page<PublicationDto> pageDto) {
        return Observable
                .from(pageDto.getContent())
                .concatMap(dto ->
                        Observable.zip(
                                getAuthors(dto.getAuthorIds()),
                                mPublisherRepository.findById(dto.getPublisherId()),
                                (authors, publisher) -> Publication.from(dto, authors, publisher)))

                .toList()
                .map(list -> new Page<>(list, pageDto.getMetadata()));
    }

    private Observable<List<Author>> getAuthors(List<String> ids) {
        return Observable
                .from(ids)
                .concatMap(mAuthorRepository::findById)
                .toList();
    }

}
