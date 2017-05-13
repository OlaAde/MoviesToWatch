package com.example.adeogo.moviestowatch;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeogo.moviestowatch.adapters.ReviewAdapter;
import com.example.adeogo.moviestowatch.adapters.TrailerAdapter;
import com.example.adeogo.moviestowatch.data.FavoritesContract;
import com.example.adeogo.moviestowatch.models.Review;
import com.example.adeogo.moviestowatch.models.Trailer;
import com.example.adeogo.moviestowatch.utilities.JsonFormatting;
import com.example.adeogo.moviestowatch.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnclickHandler, ReviewAdapter.ReviewAdapterOnclickHandler{
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private TextView mUsersRatingTextView;
    private TextView mRealeaseDateTextView;
    private ImageView mThumbnailImageView;
    private String basePicasso = "http://image.tmdb.org/t/p/w185/";
    private String id_movie;
    private RecyclerView mRecyclerViewTrailer;
    private RecyclerView mRecyclerViewReview;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private String keyPref = null;
    private Trailer mFirstTrailer = null;
    private String movieTitle;
    private int condition_favorite;
    private ContentValues movieValues = new ContentValues();
    private String selection = FavoritesContract.FavoritesEntry.COLUMN_ID_MOVIE + "=?";
    private static String[] selectionArgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleTextView = (TextView) findViewById(R.id.detail_tv);
        mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        mUsersRatingTextView = (TextView) findViewById(R.id.users_rating_tv);
        mRealeaseDateTextView = (TextView) findViewById(R.id.release_date_tv);
        mThumbnailImageView = (ImageView) findViewById(R.id.detail_iv);
        mRecyclerViewTrailer = (RecyclerView) findViewById(R.id.rv_layout_trailers);
        mRecyclerViewReview = (RecyclerView) findViewById(R.id.rv_reviews);
        mRecyclerViewTrailer.setLayoutManager(new LinearLayoutManager(this,1,false));
        mRecyclerViewReview.setLayoutManager(new LinearLayoutManager(this,1,false));
        mTrailerAdapter = new TrailerAdapter(this);
        mReviewAdapter = new ReviewAdapter(this);
        keyPref = getString((R.string.tmdb_api_key));
        mRecyclerViewTrailer.setAdapter(mTrailerAdapter);
        mRecyclerViewReview.setAdapter(mReviewAdapter);


        Intent intent = null;
        if(getIntent() != null)
            intent = getIntent();
        else{
            Toast.makeText(this,getText(R.string.could_not_show_details) , Toast.LENGTH_SHORT).show();
            finish();
        }

        movieTitle = intent.getStringExtra("movieTitle");
        String movieDescription = intent.getStringExtra("description");
        String usersRating = intent.getStringExtra("usersRating");
        String releaseDate = intent.getStringExtra("releaseDate");
        String thumbnail = intent.getStringExtra("thumbnail");
        String poster_path = intent.getStringExtra("poster_path");
        condition_favorite = intent.getIntExtra("condition_favorites", 0);
        id_movie = intent.getStringExtra("id_movie");
        setTitle(movieTitle);

        Uri uri = Uri.parse(basePicasso + thumbnail);
        Picasso.with(this).load(uri).into(mThumbnailImageView);

        mTitleTextView.setText(movieTitle);
        mDescriptionTextView.setText(movieDescription);
        mUsersRatingTextView.setText(usersRating);
        mRealeaseDateTextView.setText(releaseDate );

        TrailerTask trailerTask = new TrailerTask();
        trailerTask.execute(id_movie);

        ReviewTask reviewTask = new ReviewTask();
        reviewTask.execute(id_movie);

        selectionArgs = new String[]{id_movie};
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_TITLE, movieTitle);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_ID_MOVIE, id_movie);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, poster_path);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_DESCRIPTION, movieDescription);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, usersRating);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);
        movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_THUMBNAIL, thumbnail);
    }



    @Override
    public void onBackPressed() {
            finish();
        super.onBackPressed();
    }

    @Override
    public void clickTrailer(Uri uri) {
        Intent intent1 = new Intent(Intent.ACTION_VIEW,uri );
        startActivity(intent1);
    }

    @Override
    public void clickReview(String uriString, String string) {
        Intent intent  = new Intent(this, FullReview.class);
        intent.putExtra("link_review", uriString);
        intent.putExtra("text_review", string);
        startActivity(intent);
    }


    class TrailerTask extends AsyncTask<String, Void, List<Trailer>>{

        @Override
        protected List<Trailer> doInBackground(String... strings) {

            if (strings.length <  1 || strings[0]== null) {
                return null;
            }


            List<Trailer> trailers = null;
            try {
                trailers   =  JsonFormatting.formatTrailerJson(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildTrailerUrl(strings[0],keyPref)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (trailers== null)
                return null;
            else
                return trailers;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            if (trailers != null && !trailers.isEmpty())
            {
                mTrailerAdapter.setMovieData(trailers);
                mFirstTrailer = trailers.get(0);
            }
        }
    }
    class ReviewTask extends AsyncTask<String, Void, List<Review>>{

        @Override
        protected List<Review> doInBackground(String... strings) {

            if (strings.length <  1 || strings[0]== null) {
                return null;
            }


            List<Review> reviews = null;
            try {
                reviews   =  JsonFormatting.formatReviewJson(NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildReviewsUrl(strings[0],keyPref)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reviews== null)
                return null;
            else
                return reviews;
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            if (reviews != null && !reviews.isEmpty())
            {
                mReviewAdapter.setMovieData(reviews);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        if(condition_favorite == 1){
            menu.findItem(R.id.action_favorites).setIcon(R.drawable.ic_star_black_48dp);
        }
        else
            menu.findItem(R.id.action_favorites).setIcon(R.drawable.ic_star_white_48dp);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idSelected = item.getItemId();
        switch (idSelected){
            case R.id.action_share:
                String firstTrailerUrlPart = mFirstTrailer.getmVideoUrlPart();
                String fullStringUrl = NetworkUtils.buildYoutubeUrl(firstTrailerUrlPart).toString();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Check out this awesome movie "+ movieTitle + ", and view the trailer " + fullStringUrl);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,getResources().getText(R.string.share_to)));
                break;
            case R.id.action_favorites:
                if(condition_favorite == 1){
                    // unfavorited as it is checked already
                    condition_favorite = 0;

                    movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_FAVORITE,condition_favorite);
                    int updatedMovie =  getContentResolver().update(FavoritesContract.FavoritesEntry
                            .CONTENT_URI,movieValues,selection,selectionArgs);
                    Log.v("Movie Updated" ,""+ updatedMovie );
                    item.setIcon(R.drawable.ic_star_white_48dp);
                    Toast.makeText(this,getText(R.string.removing_from_favorites),Toast.LENGTH_SHORT).show();
                }

                else{
                    //Going to be made favorites as it is not checked
                    condition_favorite = 1;
                    movieValues.put(FavoritesContract.FavoritesEntry.COLUMN_FAVORITE,condition_favorite);
                    int updatedMovie =  getContentResolver().update(FavoritesContract.FavoritesEntry
                            .CONTENT_URI,movieValues,selection,selectionArgs);
                    Log.v("Movie Updated" ,""+ updatedMovie );
                    item.setIcon(R.drawable.ic_star_black_48dp);

                    Toast.makeText(this, getText(R.string.adding_to_favorites), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

