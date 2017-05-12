package com.example.adeogo.moviestowatch.sync;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.adeogo.moviestowatch.R;
import com.example.adeogo.moviestowatch.data.FavoritesContract;
import com.example.adeogo.moviestowatch.utilities.JsonFormatting;
import com.example.adeogo.moviestowatch.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Adeogo on 5/10/2017.
 */

public class MovieSyncTask {
    private static String keyPref = null;
    public static final String sort_by_popularity = "popular";
    public static final String sort_by_highest_rated = "top_rated";
    private static String[] selectionArgs  = { "0" };
    private static String mSortPref;

    synchronized public static void syncMovies(Context context, String action){
        keyPref = context.getString((R.string.tmdb_api_key));
        if(sort_by_highest_rated.equals(action))
            mSortPref = sort_by_highest_rated;
        else if (sort_by_popularity.equals(action))
            mSortPref = sort_by_popularity;
        URL url = NetworkUtils.buildUrl(mSortPref,keyPref);
        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            ContentValues[] moviesValues = JsonFormatting.getMoviesContentValuesFromJSON(jsonResponse);

            if(moviesValues!=null && moviesValues.length!=0){
                ContentResolver movieContentResolver = context.getContentResolver();

                movieContentResolver.delete(
                        FavoritesContract.FavoritesEntry.CONTENT_URI,
                        FavoritesContract.FavoritesEntry.COLUMN_FAVORITE +  "=?",
                        selectionArgs);

                movieContentResolver.bulkInsert(
                        FavoritesContract.FavoritesEntry.CONTENT_URI,
                        moviesValues);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

