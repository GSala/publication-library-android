package edu.upc.mcia.publications.data.remote;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class PageMetadata {

    public abstract long totalElements();

    public abstract int totalPages();

    public abstract int page();

    public abstract int size();

    public abstract boolean last();

    public static PageMetadata create(long elements, int pages, int page, int size, boolean last) {
        return new AutoValue_PageMetadata(elements, pages, page, size, last);
    }

    public static TypeAdapter<PageMetadata> typeAdapter(Gson gson) {
        return new AutoValue_PageMetadata.GsonTypeAdapter(gson);
    }
}
