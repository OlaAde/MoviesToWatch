package com.example.adeogo.moviestowatch;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.adeogo.moviestowatch.adapters.MovieAdapter;
import com.example.adeogo.moviestowatch.data.FavoritesContract;
import com.example.adeogo.moviestowatch.sync.MovieSyncIntentService;
import com.example.adeogo.moviestowatch.sync.MovieSyncTask;
import com.example.adeogo.moviestowatch.sync.MovieSyncUtils;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnclickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private static final int FAVORITES_LOADER_ID = 1;
    private Parcelable mListState;


    private String sort_by_popularity = "popular";
    private String sort_by_highest_rated = "top_rated";
    private String sort_by_favorites = "favorites";
    private String mSortPref = null;
    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null && savedInstanceState.containsKey("storeSortPref"))
        {
            String pref = savedInstanceState.getString("storeSortPref");
            mSortPref = pref;
        }
        else
        mSortPref = sort_by_favorites;

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        } else {
            mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(5, 1);
            mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        }

        mRecyclerView.setHasFixedSize(true);
        mMovieAdapter = new MovieAdapter(this,this);

        Intent intent = new Intent(this, MovieSyncIntentService.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("sortPref", mSortPref );
        intent.putExtras(mBundle);
        mRecyclerView.setAdapter(mMovieAdapter);
        syncMovies();
        Stetho.initializeWithDefaults(this);
        updateLayout();
    }


    public void syncMovies() {
        Intent incrementWaterCountIntent = new Intent(this, MovieSyncIntentService.class);
        if(mSortPref == sort_by_highest_rated)
            incrementWaterCountIntent.setAction(MovieSyncTask.sort_by_highest_rated);
        else if (mSortPref == sort_by_popularity)
            incrementWaterCountIntent.setAction(MovieSyncTask.sort_by_popularity);
        startService(incrementWaterCountIntent);
    }

    public void updateLayoutSwitches() {

        syncMovies();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("storeSortPref", mSortPref);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String[]> loader = loaderManager.getLoader(FAVORITES_LOADER_ID);
        showMovieDataView();
        mMovieAdapter.swapCursor(null);
        if (loader == null)
            loaderManager.initLoader(FAVORITES_LOADER_ID, queryBundle, new CursorCallback());
        else
            loaderManager.restartLoader(FAVORITES_LOADER_ID, queryBundle, new CursorCallback());

    }

    public void updateLayout() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("storeSortPref", mSortPref);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(FAVORITES_LOADER_ID, queryBundle, new CursorCallback());
    }

    /**
     * This method will make the View for the weather data visible and
     * hide the error message.
     */
    private void showMovieDataView() {
        /* First, to make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, to make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }




    public class CursorCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            mLoadingIndicator.setVisibility(View.VISIBLE);

            String selection = null;
            String[] selectionArgs = null;
            if (mSortPref == sort_by_favorites){
                selection  =   FavoritesContract.FavoritesEntry.COLUMN_FAVORITE +  "=?";
                selectionArgs = new String[]{"1"};
            }
            else {
                selection  =   FavoritesContract.FavoritesEntry.COLUMN_FAVORITE +  "!=?";
                selectionArgs = new String[]{"1"};
            }
            switch (FAVORITES_LOADER_ID) {

                case FAVORITES_LOADER_ID:
                /* URI for all rows of weather data in our weather table */
                    Uri forecastQueryUri = FavoritesContract.FavoritesEntry.CONTENT_URI;
                /* Sort order: Ascending by date */
                    String sortOrder = FavoritesContract.FavoritesEntry._ID + " ASC";
                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */

                    return new CursorLoader(MainActivity.this,
                            forecastQueryUri,
                            null,
                            selection,
                            selectionArgs,
                            sortOrder);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + FAVORITES_LOADER_ID);
            }
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieAdapter.swapCursor(data);
            mStaggeredGridLayoutManager.onRestoreInstanceState(mListState);
            if (data.getCount() != 0) showMovieDataView();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mMovieAdapter.swapCursor(null);
        }
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     */

    @Override
    public void voidMethod(Cursor mCursor, int adapterPosition) {
        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);

        mCursor.moveToPosition(adapterPosition);
        String movieTitle = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE));
        String description = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_DESCRIPTION));
        String usersRating = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RATING));
        String releaseDate = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
        String thumbnail = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_THUMBNAIL));
        String id_movie = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ID_MOVIE));
        int condition_favorites = mCursor.getInt(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_FAVORITE));
        String poster_path = mCursor.getString(mCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH));

        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("description", description);
        intent.putExtra("usersRating", usersRating);
        intent.putExtra("releaseDate", releaseDate);
        intent.putExtra("thumbnail", thumbnail);
        intent.putExtra("id_movie", id_movie);
        intent.putExtra("condition_favorites", condition_favorites);
        intent.putExtra("poster_path", poster_path);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem action_sort_by_favorites = menu.findItem(R.id.action_sort_by_favorites);
        if (mSortPref.contentEquals(sort_by_popularity)) {
            if (!action_sort_by_popularity.isChecked())
                action_sort_by_popularity.setChecked(true);
        } else if (mSortPref.contentEquals(sort_by_highest_rated)) {
            if (!action_sort_by_rating.isChecked())
                action_sort_by_rating.setChecked(true);
        } else if (mSortPref.contentEquals(sort_by_favorites)) {
            if (!action_sort_by_favorites.isChecked())
                action_sort_by_favorites.setChecked(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenuSelected = item.getItemId();
        switch (idMenuSelected) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked())
                    item.setChecked(false);
                else{
                    item.setChecked(true);
                    mSortPref = sort_by_popularity;
                    MovieSyncUtils.startImmediateSync(this);
                    updateLayoutSwitches();
                }
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked())
                    item.setChecked(false);
                else{
                    item.setChecked(true);
                    mSortPref = sort_by_highest_rated;
                    MovieSyncUtils.startImmediateSync(this);
                    updateLayoutSwitches();
                }
                return true;
            case R.id.action_sort_by_favorites:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortPref = sort_by_favorites;
                updateLayoutSwitches();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mStaggeredGridLayoutManager.onSaveInstanceState();
        outState.putParcelable("list_state", mListState);
        outState.putString("storeSortPref", mSortPref);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable("storeSortPref");
    }

}


