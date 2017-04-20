package net.slomnicki.udacity.popularmovies.movieposters;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMoviesResponse;

import java.util.List;

public class PostersLoader extends AsyncTaskLoader<List<TmdbMovie>> {
    public static final int SORT_POPULAR = 1;
    public static final int SORT_RATING = 2;
    private static final String TAG = PostersLoader.class.getSimpleName();

    private List<TmdbMovie> mResults;
    private int mMode;

    public PostersLoader(Context context, int mode) {
        super(context);
        mMode = mode;
    }

    @Override
    protected void onStartLoading() {
        if (mResults == null)
            forceLoad();
        else
            deliverResult(mResults);
    }

    @Override
    public void deliverResult(List<TmdbMovie> data) {
        mResults = data;
        super.deliverResult(data);
    }

    @Override
    public List<TmdbMovie> loadInBackground() {
        Log.d(TAG, "loadInBackground: Start");
        MovieDatabaseApi api = new MovieDatabaseApi();
        TmdbMoviesResponse response = null;
        if (mMode == SORT_POPULAR)
            response = api.getMoviesByPopularity();
        else if (mMode == SORT_RATING)
            response = api.getMoviesByRating();
        Log.d(TAG, "loadInBackground: END");
        return response == null ? null : response.getResults();
    }
}