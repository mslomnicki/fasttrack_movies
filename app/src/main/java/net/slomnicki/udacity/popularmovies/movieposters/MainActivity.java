package net.slomnicki.udacity.popularmovies.movieposters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMoviesResponse;
import net.slomnicki.udacity.popularmovies.moviedetails.DetailsActivity;
import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PostersAdapter.OnPosterClickListener {

    private static final String BUNDLE_SORTING = MainActivity.class.getName() + "_SORTING";
    private static final String BUNDLE_MOVIES = MainActivity.class.getName() + "_MOVIES";
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mPostersRecyclerView;
    private PostersAdapter mPostersAdapter;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;
    private int mSortOrder = R.id.action_sort_popular;
    private List<TmdbMovie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        initializePostersRecyclerView();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onCreate: Bundle available, restoring");
        mSortOrder = savedInstanceState.getInt(BUNDLE_SORTING);
        mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIES);
        setRecyclerViewMovieList(mMovies);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMovies == null) fetchPosters();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_SORTING, mSortOrder);
        if (mMovies != null) {
            outState.putParcelableArrayList(BUNDLE_MOVIES, new ArrayList<Parcelable>(mMovies));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                mSortOrder = R.id.action_sort_popular;
                break;
            case R.id.action_sort_rating:
                mSortOrder = R.id.action_sort_rating;
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        fetchPosters();
        return true;
    }

    private void initializePostersRecyclerView() {
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mPostersRecyclerView.setLayoutManager(layoutManager);
        mPostersRecyclerView.setHasFixedSize(true);
        mPostersAdapter = new PostersAdapter(this);
        mPostersRecyclerView.setAdapter(mPostersAdapter);
    }

    private void fetchPosters() {
        if (NetworkUtils.isOnline(this)) {
            new PostersFetcher().execute(mSortOrder);
        } else {
            showErrorMessage();
        }
    }

    private void setRecyclerViewMovieList(List<TmdbMovie> movies) {
        mMovies = movies;
        mPostersAdapter.setMovieList(mMovies);
    }

    private class PostersFetcher extends AsyncTask<Integer, Void, List<TmdbMovie>> {
        @Override
        protected List<TmdbMovie> doInBackground(Integer... params) {
            MovieDatabaseApi api = new MovieDatabaseApi();
            TmdbMoviesResponse response = null;
            if (params == null || params.length == 0 || params[0] == R.id.action_sort_popular)
                response = api.getMoviesByPopularity();
            else if (params[0] == R.id.action_sort_rating)
                response = api.getMoviesByRating();
            return response.getResults();
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected void onPostExecute(List<TmdbMovie> movies) {
            setRecyclerViewMovieList(movies);
            showRecyclerView();
        }
    }

    private void showErrorMessage() {
        mPostersRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mPostersRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        mPostersRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPosterClick(TmdbMovie movie) {
        DetailsActivity.startActivity(this, movie);
    }
}
