package edu.upc.mcia.publications.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import rx.functions.Func1;

@AutoValue
public abstract class Author implements Comparable<Author> {

    @ColumnName("_id")
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

    public static Author create(Cursor cursor) {
        return AutoValue_Author.createFromCursor(cursor);
    }

    // Optional: if your project includes RxJava the extension will generate a Func1<Cursor, User>
    public static Func1<Cursor, Author> mapper() {
        return AutoValue_Author.MAPPER;
    }

    // Optional: When you include an abstract method that returns ContentValues and doesn't have
    // any parameters the extension will implement it for you
    public abstract ContentValues toContentValues();

    @Override
    public int compareTo(Author author) {
        return fullname().compareTo(author.fullname());
    }
}
