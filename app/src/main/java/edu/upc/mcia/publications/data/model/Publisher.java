package edu.upc.mcia.publications.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.gabrielittner.auto.value.cursor.ColumnName;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import rx.functions.Func1;

@AutoValue
public abstract class Publisher {

    @ColumnName("_id")
    public abstract String id();

    public abstract String type();

    public abstract String fullname();

    public abstract String acronym();

    public static Publisher create(String id, String type, String fullname, String acronym) {
        return new AutoValue_Publisher(id, type, fullname, acronym);
    }

    public static TypeAdapter<Publisher> typeAdapter(Gson gson) {
        return new AutoValue_Publisher.GsonTypeAdapter(gson);
    }

    public static Publisher create(Cursor cursor) {
        return AutoValue_Publisher.createFromCursor(cursor);
    }

    // Optional: if your project includes RxJava the extension will generate a Func1<Cursor, User>
    public static Func1<Cursor, Publisher> mapper() {
        return AutoValue_Publisher.MAPPER;
    }

    // Optional: When you include an abstract method that returns ContentValues and doesn't have
    // any parameters the extension will implement it for you
    public abstract ContentValues toContentValues();
}
