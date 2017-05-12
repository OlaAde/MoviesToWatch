package com.example.adeogo.moviestowatch.utilities;

import android.content.ContentValues;
import android.text.TextUtils;
import android.util.Log;

import com.example.adeogo.moviestowatch.data.FavoritesContract;
import com.example.adeogo.moviestowatch.models.Review;
import com.example.adeogo.moviestowatch.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adeogo on 5/10/2017.
 */

public class JsonFormatting {

    public static List<Review> formatReviewJson(String jsonData) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }

        List<Review> reviews = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray reviewsArray = jsonObject.getJSONArray("results");


            for (int i = 0; i < reviewsArray.length(); i++) {

                JSONObject position = reviewsArray.getJSONObject(i);

                String authorName = position.getString("author");
                String reviewText = position.getString("content");
                String reviewLink = position.getString("url");

                // Create a new GitHubUser object with the username, photoUrl, profileUrl.
                reviews.add(new Review(authorName,reviewText,reviewLink));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the trailers JSON results", e);
        }

        // Return the list of gitHubUsers
        return reviews;
    }
    public static ContentValues[] getMoviesContentValuesFromJSON(String JSONData) throws JSONException {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(JSONData)) {
            return null;
        }

        JSONObject jsonObject = new JSONObject(JSONData);
        JSONArray moviesArray = jsonObject.getJSONArray("results");

        ContentValues[] moviesContentValues = new ContentValues[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject position = moviesArray.getJSONObject(i);

            String titleMovie = position.getString("original_title");
            String idMovie = position.getString("id");
            String posterPath = position.getString("backdrop_path");
            String description = position.getString("overview");
            String rating = position.getString("vote_average");
            String releaseDate = position.getString("release_date");
            String thumbnail = position.getString("backdrop_path");
            int favorited = 0;

            ContentValues movieValues = new ContentValues();
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, titleMovie);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_ID_MOVIE, idMovie);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, posterPath);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_DESCRIPTION, description);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, rating);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_THUMBNAIL, thumbnail);
            movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_FAVORITE,favorited);

            moviesContentValues[i] = movieValues;

        }
        return moviesContentValues;
    }
    public static List<Trailer> formatTrailerJson(String jsonData) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonData)) {
            return null;
        }

        List<Trailer> trailers = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray trailersArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++) {

                JSONObject position = trailersArray.getJSONObject(i);

                String trailerName = position.getString("name");
                String videoPart = position.getString("key");

                // Create a new GitHubUser object with the username, photoUrl, profileUrl.
                trailers.add(new Trailer(trailerName,videoPart));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the trailers JSON results", e);
        }

        // Return the list of gitHubUsers
        return trailers;
    }

}

