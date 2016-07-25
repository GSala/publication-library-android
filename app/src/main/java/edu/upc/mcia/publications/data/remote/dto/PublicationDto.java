package edu.upc.mcia.publications.data.remote.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class PublicationDto {

    private String id;
    private Date publishDate;
    private String title;
    private String summary;
    private List<String> authorIds;
    private String publisherId;
    private String url;
    private String reference;
    private float score;

}
