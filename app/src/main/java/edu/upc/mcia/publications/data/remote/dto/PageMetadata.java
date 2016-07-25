package edu.upc.mcia.publications.data.remote.dto;

import lombok.Data;

@Data
public class PageMetadata {

    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
    private boolean last;

}
