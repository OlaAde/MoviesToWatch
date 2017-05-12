package com.example.adeogo.moviestowatch.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adeogo on 4/26/2017.
 */

public class FavoritesContract {

    private FavoritesContract(){

    }
    public static final String AUTHORITY = "com.example.adeogo.moviestowatch";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASKS = "favorites";


    public static final class FavoritesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ID_MOVIE = "id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_FAVORITE = "favorited";
    }
}


