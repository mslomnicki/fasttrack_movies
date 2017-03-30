package net.slomnicki.udacity.popularmovies.movieposters;

import android.os.AsyncTask;

import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMoviesResponse;
import net.slomnicki.udacity.popularmovies.utils.AsyncTaskCompleteListener;

import java.util.List;

public class PostersFetcher extends AsyncTask<Integer, Void, List<TmdbMovie>> {
    public static final int SORT_POPULAR = 1;
    public static final int SORT_RATING = 2;

    private AsyncTaskCompleteListener<List<TmdbMovie>> mListener;

    public PostersFetcher(AsyncTaskCompleteListener<List<TmdbMovie>> listener) {
        mListener = listener;
    }

    @Override
    protected List<TmdbMovie> doInBackground(Integer... params) {
        MovieDatabaseApi api = new MovieDatabaseApi();
        TmdbMoviesResponse response = null;
        if (params == null || params.length == 0 || params[0] == SORT_POPULAR)
            response = api.getMoviesByPopularity();
        else if (params[0] == SORT_RATING)
            response = api.getMoviesByRating();
        return response == null ? null : response.getResults();
    }

    @Override
    protected void onPostExecute(List<TmdbMovie> tmdbMovies) {
        super.onPostExecute(tmdbMovies);
        mListener.onTaskComplete(tmdbMovies);
    }
}