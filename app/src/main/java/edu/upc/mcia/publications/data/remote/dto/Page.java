package edu.upc.mcia.publications.data.remote.dto;

import java.util.List;

public class Page<T> {

    private List<T> content;
    private PageMetadata metadata;

    public Page() {
    }

    public Page(List<T> content, PageMetadata metadata) {
        this.content = content;
        this.metadata = metadata;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PageMetadata metadata) {
        this.metadata = metadata;
    }
}
