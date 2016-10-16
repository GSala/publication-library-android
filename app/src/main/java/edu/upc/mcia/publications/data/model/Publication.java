package edu.upc.mcia.publications.data.model;

import java.util.Date;
import java.util.List;

import edu.upc.mcia.publications.data.remote.dto.PublicationDto;

public class Publication {

    private String id;
    private Date publishDate;
    private String title;
    private String summary;
    private List<Author> authors;
    private Publisher publisher;
    private String url;
    private String reference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

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
