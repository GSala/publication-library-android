package edu.upc.mcia.publications.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class Publication {

    public abstract String id();

    public abstract Date publishDate();

    public abstract String title();

    public abstract String summary();

    public abstract List<String> authorIds();

    public abstract String publisherId();

    public abstract String url();

    public abstract String reference();

    public abstract float score();

    public static Publication create(String id, Date publishDate, String title, String summary, List<String> authorIds, String publisherId, String url, String reference, float score) {
        return new AutoValue_Publication(id, publishDate, title, summary, authorIds, publisherId, url, reference, score);
    }

    public static TypeAdapter<Publication> typeAdapter(Gson gson) {
        return new AutoValue_Publication.GsonTypeAdapter(gson);
    }
}
