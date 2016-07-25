package edu.upc.mcia.publications.data.model;

import lombok.Data;

@Data
public class Author implements Comparable<Author> {

    private String id;
    private String fullname;
    private String email;
    private String photo;

    @Override
    public int compareTo(Author author) {
        return fullname.compareTo(author.getFullname());
    }
}
