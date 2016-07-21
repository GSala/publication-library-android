package edu.upc.mcia.publications.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mciapub.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String NUMBER_TYPE = " INTEGER";
    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_AUTHOR_ENTRIES =
            "CREATE TABLE " + AuthorEntry.TABLE_NAME + " (" +
                    AuthorEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    AuthorEntry.COLUMN_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    AuthorEntry.COLUMN_EMAIL + TEXT_TYPE + COMMA_SEP +
                    AuthorEntry.COLUMN_PHOTO + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_PUBLISHER_ENTRIES =
            "CREATE TABLE " + PublisherEntry.TABLE_NAME + " (" +
                    PublisherEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    PublisherEntry.COLUMN_FULLNAME + TEXT_TYPE + COMMA_SEP +
                    PublisherEntry.COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
                    PublisherEntry.COLUMN_ACRONYM + TEXT_TYPE +
                    " )";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_AUTHOR_ENTRIES);
        db.execSQL(SQL_CREATE_PUBLISHER_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public static abstract class AuthorEntry implements BaseColumns {
        public static final String TABLE_NAME = "authors";
        public static final String COLUMN_FULLNAME = "fullname";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHOTO = "photo";
    }

    public static final class PublisherEntry implements BaseColumns {
        public static final String TABLE_NAME = "publishers";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FULLNAME = "fullname";
        public static final String COLUMN_ACRONYM = "acronym";
    }

}
