package net.slomnicki.udacity.popularmovies.moviedetails;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieReview;

import java.util.List;

class ReviewsLoader extends AsyncTaskLoader<List<TmdbMovieReview>> {
    List<TmdbMovieReview> mReviews;
    private int mMovieId;

    public ReviewsLoader(Context context, int movieId) {
        super(context);
        mMovieId = movieId;
    }

    @Override
    public void deliverResult(List<TmdbMovieReview> data) {
        if (mReviews == null) mReviews = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if (mReviews != null)
            deliverResult(mReviews);
        else
            forceLoad();
    }

    @Override
    public List<TmdbMovieReview> loadInBackground() {
        MovieDatabaseApi api = new MovieDatabaseApi();
        return api.fetchMovieReviews(mMovieId);
    }
}