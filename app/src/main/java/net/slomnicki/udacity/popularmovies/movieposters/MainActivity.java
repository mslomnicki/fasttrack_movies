package net.slomnicki.udacity.popularmovies.movieposters;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMoviesResponse;
import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostersRecyclerView;
    private PostersAdapter mPostersAdapter;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
        initializePostersRecyclerView();
        fetchPosters();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                new PostersFetcher().execute(R.id.action_sort_popular);
                return true;
            case R.id.action_sort_rating:
                new PostersFetcher().execute(R.id.action_sort_rating);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    private void initializePostersRecyclerView() {
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mPostersRecyclerView.setLayoutManager(layoutManager);
//        mPostersRecyclerView.setHasFixedSize(true);
        mPostersAdapter = new PostersAdapter();
        mPostersRecyclerView.setAdapter(mPostersAdapter);
    }

    private void fetchPosters() {
        if (NetworkUtils.isOnline(this)) {
            new PostersFetcher().execute();
        } else {
            showErrorMessage();
        }
    }

    private class PostersFetcher extends AsyncTask<Integer, Void, List<TmdbMovie>> {
        @Override
        protected List<TmdbMovie> doInBackground(Integer... params) {
            MovieDatabaseApi api = new MovieDatabaseApi();
            TmdbMoviesResponse response = null;
            if(params == null || params.length == 0 || params[0] == R.id.action_sort_popular)
                response = api.getMoviesByPopularity();
            else if(params[0] == R.id.action_sort_rating)
             response = api.getMoviesByRating();
            return response.getResults();
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected void onPostExecute(List<TmdbMovie> tmdbMovies) {
            mPostersAdapter.setMovieList(tmdbMovies);
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
}
