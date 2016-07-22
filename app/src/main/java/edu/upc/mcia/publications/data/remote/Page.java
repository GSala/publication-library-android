package edu.upc.mcia.publications.data.remote;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.List;

import edu.upc.mcia.publications.data.model.Publication;

@AutoValue
public abstract class Page {

    public abstract List<Publication> content();

    public abstract PageMetadata metadata();

    public static Page create(List<Publication> data, PageMetadata metadata) {
        return new AutoValue_Page(data, metadata);
    }

    public static TypeAdapter<Page> typeAdapter(Gson gson) {
        return new AutoValue_Page.GsonTypeAdapter(gson);
    }
}
