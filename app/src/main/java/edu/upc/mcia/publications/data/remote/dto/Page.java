package edu.upc.mcia.publications.data.remote.dto;

import java.util.List;

import edu.upc.mcia.publications.data.model.Publication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {

    private List<T> content;
    private PageMetadata metadata;

}
