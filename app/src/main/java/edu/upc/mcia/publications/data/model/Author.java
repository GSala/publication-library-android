package edu.upc.mcia.publications.data.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract  class Author {

    public abstract String id();
    public abstract String fullname();
    public abstract String email();
    public abstract String photo();


    public static Author create(String id, String fullname, String email, String photo) {
        return new AutoValue_Author(id, fullname, email, photo);
    }

    public static TypeAdapter<Author> typeAdapter(Gson gson) {
        return new AutoValue_Author.GsonTypeAdapter(gson);
    }

}
