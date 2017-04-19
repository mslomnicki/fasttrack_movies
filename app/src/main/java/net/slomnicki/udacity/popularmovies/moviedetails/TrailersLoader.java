package net.slomnicki.udacity.popularmovies.moviedetails;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieTrailer;

import java.util.List;


public class TrailersLoader extends AsyncTaskLoader<List<TmdbMovieTrailer>> {
    private static final String TAG = TrailersLoader.class.getSimpleName();
    private int mMovieId;
    private List<TmdbMovieTrailer> mTrailers;

    public TrailersLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    public void deliverResult(List<TmdbMovieTrailer> data) {
        Log.d(TAG, "deliverResult: ");
        if (mTrailers == null) mTrailers = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        if (mTrailers == null) {
            forceLoad();
        } else {
            deliverResult(mTrailers);
        }

    }

    @Override
    public List<TmdbMovieTrailer> loadInBackground() {
        Log.d(TAG, "loadInBackground: load trailers");
        MovieDatabaseApi api = new MovieDatabaseApi();
        return api.fetchMovieTrailers(mMovieId);
    }
}
