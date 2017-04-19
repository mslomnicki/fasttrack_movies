package net.slomnicki.udacity.popularmovies.movieposters;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.moviedetails.DetailsActivity;
import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements PostersAdapter.OnPosterClickListener, LoaderManager.LoaderCallbacks<List<TmdbMovie>> {

    private static final String BUNDLE_SORTING = MainActivity.class.getName() + "_SORTING";
    private static final String BUNDLE_MOVIES = MainActivity.class.getName() + "_MOVIES";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIES_LOADER_ID = 7762;

    private RecyclerView mPostersRecyclerView;
    private PostersAdapter mPostersAdapter;

    private TextView mErrorMessage;
    private int mSortOrder = R.id.action_sort_popular;
    private List<TmdbMovie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mErrorMessage = (TextView) findViewById(R.id.tv_error_message);
        initializePostersRecyclerView();
    }

    private void initializePostersRecyclerView() {
        int orientation = getResources().getConfiguration().orientation;
        int spanCount = orientation == Configuration.ORIENTATION_PORTRAIT ? 2 : 4;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mPostersRecyclerView.setLayoutManager(layoutManager);
        mPostersRecyclerView.setHasFixedSize(true);
        mPostersAdapter = new PostersAdapter(this);
        mPostersRecyclerView.setAdapter(mPostersAdapter);
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

    private void fetchPosters() {
        if (NetworkUtils.isOnline(this)) {
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
        } else {
            showErrorMessage();
        }
    }

    private void setRecyclerViewMovieList(List<TmdbMovie> movies) {
        mMovies = movies;
        mPostersAdapter.setMovieList(mMovies);
    }

    private void showErrorMessage() {
        mPostersRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView() {
        mPostersRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPosterClick(TmdbMovie movie) {
        DetailsActivity.startActivity(this, movie);
    }

    @Override
    public Loader<List<TmdbMovie>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        int sortOrder = mSortOrder == R.id.action_sort_popular ?
                PostersLoader.SORT_POPULAR : PostersLoader.SORT_RATING;
        return new PostersLoader(this, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<List<TmdbMovie>> loader, List<TmdbMovie> movies) {
        Log.d(TAG, "onLoadFinished: ");
        setRecyclerViewMovieList(movies);
        showRecyclerView();
    }

    @Override

    public void onLoaderReset(Loader<List<TmdbMovie>> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}
