package edu.upc.mcia.publications.data.repository

import edu.upc.mcia.publications.data.model.Author
import edu.upc.mcia.publications.data.model.Publication
import edu.upc.mcia.publications.data.model.Publisher
import edu.upc.mcia.publications.data.remote.ApiManager
import edu.upc.mcia.publications.data.remote.dto.Page
import edu.upc.mcia.publications.data.remote.dto.PublicationDto
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*

object PublicationRepository {

    private val mAuthorRepository: AuthorRepository
    private val mPublisherRepository: PublisherRepository

    private val mData = HashMap<String, Publication>()

    init {
        mAuthorRepository = AuthorRepository
        mPublisherRepository = PublisherRepository.getInstance()
    }

    fun search(pageNumber: Int, query: String): Observable<Page<Publication>> {
        return ApiManager.remoteApi.getPublications(pageNumber, query)
                .flatMap({ this.fillPublicationPage(it) })
                .doOnNext { mData += it.content.map { it.id to it } }
    }

    fun findById(publicationId: String): Observable<Publication> {
        mData[publicationId]?.let { return Observable.just(it) } ?:
                return Observable.just(publicationId)
                        .flatMap { ApiManager.remoteApi.getPublication(it) }
                        .flatMap { fillPublication(it) }
    }

    private fun fillPublication(dto: PublicationDto): Observable<Publication> {
        return Observable.zip<List<Author>, Publisher, Publication>(
                getAuthors(dto.authorIds),
                mPublisherRepository.findById(dto.publisherId),
                BiFunction { authors, publisher -> Publication.from(dto, authors, publisher) })

    }

    private fun fillPublicationPage(pageDto: Page<PublicationDto>): Observable<Page<Publication>> {
        return Observable
                .fromIterable(pageDto.content)
                .concatMap(this::fillPublication)
                .toList()
                .toObservable()
                .map { list -> Page(list, pageDto.metadata) }
    }

    private fun getAuthors(ids: List<String>): Observable<List<Author>> {
        return Observable
                .fromIterable(ids)
                .concatMap<Author>({ mAuthorRepository.findById(it) })
                .toList().toObservable()
    }

}
