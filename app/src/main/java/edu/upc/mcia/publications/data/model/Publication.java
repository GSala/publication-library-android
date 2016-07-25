package edu.upc.mcia.publications.data.model;

import java.util.Date;
import java.util.List;

import edu.upc.mcia.publications.data.remote.dto.PublicationDto;
import lombok.Data;

@Data
public class Publication {

    private String id;
    private Date publishDate;
    private String title;
    private String summary;
    private List<Author> authors;
    private Publisher publisher;
    private String url;
    private String reference;

    public static Publication from(PublicationDto dto, List<Author> authors, Publisher publisher) {
        Publication pub = new Publication();
        pub.setId(dto.getId());
        pub.setPublishDate(dto.getPublishDate());
        pub.setReference(dto.getReference());
        pub.setTitle(dto.getTitle());
        pub.setSummary(dto.getSummary());
        pub.setUrl(dto.getUrl());
        pub.setAuthors(authors);
        pub.setPublisher(publisher);
        return pub;
    }

}
