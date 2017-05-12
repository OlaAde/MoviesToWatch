package com.example.adeogo.moviestowatch.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.adeogo.moviestowatch.models.Review;
import com.example.adeogo.moviestowatch.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Adeogo on 4/25/2017.
 */

public final class NetworkUtils {
    final static String KEY_PARAM = "api_key";
    final static String PAGE_PARAM = "page";
    final static String SCHEME_URI = "https";
    final static String AUTHORITY_URI= "api.themoviedb.org";
    final static String PATH_1 = "3";

    final static String PARAM_QUERY = "v";

    final static String PATH_MOVIE = "movie";
    final static String PATH_VIDEOS = "videos";
    final static String PATH_REVIEWS = "reviews";
    final static String BASE_YOUTUBE = "https://www.youtube.com/watch";



    public static URL buildUrl(String sortPref, String keyPref){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_URI)
                .authority(AUTHORITY_URI)
                .appendPath(PATH_1)
                .appendPath(PATH_MOVIE)
                .appendPath(sortPref)
                .appendQueryParameter(KEY_PARAM, keyPref);
        Uri builtUri = builder.build();
        URL url = null;
        Log.v("Link to movies",builtUri.toString());
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildTrailerUrl(String idMovie, String keyPref){
        Uri.Builder builder = new Uri .Builder();
        builder.scheme(SCHEME_URI)
                .authority(AUTHORITY_URI)
                .appendPath(PATH_1)
                .appendPath(PATH_MOVIE)
                .appendPath(idMovie)
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(KEY_PARAM, keyPref);
        Uri builtUri = builder.build();
        Log.v("Trailers url", builtUri.toString());
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildReviewsUrl(String idMovie, String keyPref){
        Uri.Builder builder = new Uri .Builder();
        builder.scheme(SCHEME_URI)
                .authority(AUTHORITY_URI)
                .appendPath(PATH_1)
                .appendPath(PATH_MOVIE)
                .appendPath(idMovie)
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(KEY_PARAM, keyPref);
        Uri builtUri = builder.build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static Uri buildYoutubeUrl(String partBase) {
        Uri builtUri = Uri.parse(BASE_YOUTUBE).buildUpon()
                .appendQueryParameter(PARAM_QUERY, partBase)
                .build();
        return builtUri;
    }

    public static JSONArray resultArray(String movieJsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(movieJsonStr);
        JSONArray movieArray = jsonObject.getJSONArray("results");
        return movieArray;
    }


}
