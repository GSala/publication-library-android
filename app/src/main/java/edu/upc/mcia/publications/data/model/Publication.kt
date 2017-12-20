package edu.upc.mcia.publications.data.model

import edu.upc.mcia.publications.data.remote.dto.PublicationDto
import java.util.*

data class Publication(

        var id: String,
        var publishDate: Date,
        var title: String,
        var summary: String,
        var authors: List<Author>,
        var publisher: Publisher,
        var url: String,
        var reference: String) {

    companion object {

        fun from(dto: PublicationDto, authors: List<Author>, publisher: Publisher): Publication {
            return Publication(
                    dto.id,
                    dto.publishDate,
                    dto.title,
                    dto.summary,
                    authors,
                    publisher,
                    dto.url,
                    dto.reference)
        }
    }

}
