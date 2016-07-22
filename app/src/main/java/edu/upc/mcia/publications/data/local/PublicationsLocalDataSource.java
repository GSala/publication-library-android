package edu.upc.mcia.publications.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.upc.mcia.publications.App;
import edu.upc.mcia.publications.data.local.DbHelper.PublicationEntry;
import edu.upc.mcia.publications.data.model.Publication;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PublicationsLocalDataSource {
    private static final String LIST_SEPARATOR = ",";
    private static PublicationsLocalDataSource INSTANCE = null;
    private final BriteDatabase mDatabaseHelper;
    private Func1<Cursor, Publication> mPublicationMapperFunction;

    private PublicationsLocalDataSource() {
        DbHelper dbHelper = new DbHelper(App.getContext());
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());

        mPublicationMapperFunction = c -> {
            String id = c.getString(c.getColumnIndexOrThrow(PublicationEntry._ID));
            Date date = new Date(c.getLong(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_DATE)));
            String a = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_AUTHORS));
            List<String> authors = Arrays.asList(a.split(LIST_SEPARATOR));
            String title = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_TITLE));
            String summary = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_SUMMARY));
            String url = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_URL));
            String reference = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_REFERENCE));
            float score = c.getFloat(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_SCORE));
            String publisherId = c.getString(c.getColumnIndexOrThrow(PublicationEntry.COLUMN_PUBLISHER));
            return Publication.create(id, date, title, summary, authors, publisherId, url, reference, score);
        };
    }

    public static PublicationsLocalDataSource getInstance() {
        if (INSTANCE == null) {
            return new PublicationsLocalDataSource();
        }
        return INSTANCE;
    }

    public Observable<List<Publication>> getPublications() {
        String sql = String.format("SELECT * FROM %s ORDER BY date(%s)",
                DbHelper.PublicationEntry.TABLE_NAME,
                DbHelper.PublicationEntry.COLUMN_DATE);

        return mDatabaseHelper.createQuery(DbHelper.PublicationEntry.TABLE_NAME, sql)
                .mapToList(mPublicationMapperFunction);
    }

    private void savePublication(Publication p) {
        ContentValues values = new ContentValues();
        values.put(PublicationEntry._ID, p.id());
        values.put(PublicationEntry.COLUMN_AUTHORS, convertListToString(p.authorIds()));
        values.put(PublicationEntry.COLUMN_DATE, p.publishDate().getTime());
        values.put(PublicationEntry.COLUMN_PUBLISHER, p.publisherId());
        values.put(PublicationEntry.COLUMN_REFERENCE, p.reference());
        values.put(PublicationEntry.COLUMN_SCORE, p.score());
        values.put(PublicationEntry.COLUMN_SUMMARY, p.summary());
        values.put(PublicationEntry.COLUMN_URL, p.url());
        values.put(PublicationEntry.COLUMN_TITLE, p.title());
        mDatabaseHelper.insert(DbHelper.PublicationEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private String convertListToString(List<String> authors) {
        StringBuilder builder = new StringBuilder();
        for (String s : authors) {
            builder.append(s);
            builder.append(LIST_SEPARATOR);
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public void savePublications(List<Publication> publications) {
        BriteDatabase.Transaction transaction = mDatabaseHelper.newTransaction();
        for (Publication n : publications) {
            savePublication(n);
        }
        transaction.markSuccessful();
        transaction.end();
    }

}
