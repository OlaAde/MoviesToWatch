package com.example.adeogo.moviestowatch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.adeogo.moviestowatch.data.FavoritesContract.FavoritesEntry;

/**
 * Created by Adeogo on 4/27/2017.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "favoritesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;
    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create tasks table (careful to follow SQL formatting rules)

        final String CREATE_TABLE = "CREATE TABLE "  + FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_ID_MOVIE    + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_RELEASE_DATE  + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_THUMBNAIL  + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_FAVORITE + " INTEGER NOT NULL);";

        Log.v("Create_State,ent", CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
