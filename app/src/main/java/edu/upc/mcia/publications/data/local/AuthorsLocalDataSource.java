package edu.upc.mcia.publications.data.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import edu.upc.mcia.publications.App;
import edu.upc.mcia.publications.data.local.DbHelper.AuthorEntry;
import edu.upc.mcia.publications.data.model.Author;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AuthorsLocalDataSource {
    private static AuthorsLocalDataSource INSTANCE = null;
    private final BriteDatabase mDatabaseHelper;
    private Func1<Cursor, Author> mAuthorMapperFunction;

    private AuthorsLocalDataSource() {
        DbHelper dbHelper = new DbHelper(App.getContext());
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());

        mAuthorMapperFunction = Author.mapper();
    }

    public static AuthorsLocalDataSource getInstance() {
        if (INSTANCE == null) {
            return new AuthorsLocalDataSource();
        }
        return INSTANCE;
    }

    public Observable<List<Author>> getAuthors() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s",
                AuthorEntry.TABLE_NAME,
                AuthorEntry.COLUMN_FULLNAME);

        return mDatabaseHelper.createQuery(AuthorEntry.TABLE_NAME, sql)
                .mapToList(mAuthorMapperFunction);
    }

    private void saveAuthor(Author a) {
        ContentValues values = a.toContentValues();
        mDatabaseHelper.insert(AuthorEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void saveAuthors(List<Author> news) {
        BriteDatabase.Transaction transaction = mDatabaseHelper.newTransaction();
        for (Author n : news) {
            saveAuthor(n);
        }
        transaction.markSuccessful();
        transaction.end();
    }

}