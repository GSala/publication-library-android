package edu.upc.mcia.publications.data.local;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import edu.upc.mcia.publications.App;
import edu.upc.mcia.publications.data.model.Publisher;
import rx.Observable;
import rx.schedulers.Schedulers;

public class PublishersLocalDataSource {
    private static PublishersLocalDataSource INSTANCE = null;
    private final BriteDatabase mDatabaseHelper;

    private PublishersLocalDataSource() {
        DbHelper dbHelper = new DbHelper(App.getContext());
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());

    }

    public static PublishersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            return new PublishersLocalDataSource();
        }
        return INSTANCE;
    }

    public Observable<List<Publisher>> getPublishers() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s",
                DbHelper.PublisherEntry.TABLE_NAME,
                DbHelper.PublisherEntry.COLUMN_FULLNAME);

        return mDatabaseHelper.createQuery(DbHelper.PublisherEntry.TABLE_NAME, sql)
                .mapToList(Publisher.mapper());
    }

    private void savePublisher(Publisher a) {
        ContentValues values = a.toContentValues();
        mDatabaseHelper.insert(DbHelper.PublisherEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void savePublishers(List<Publisher> pubs) {
        BriteDatabase.Transaction transaction = mDatabaseHelper.newTransaction();
        for (Publisher n : pubs) {
            savePublisher(n);
        }
        transaction.markSuccessful();
        transaction.end();
    }
}