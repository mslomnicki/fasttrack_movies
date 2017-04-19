package net.slomnicki.udacity.popularmovies.api;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import net.slomnicki.udacity.popularmovies.BuildConfig;
import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MovieDatabaseApi {
    private static final String TAG = MovieDatabaseApi.class.getSimpleName();
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String PATH_SORT_POPULAR = "movie/popular";
    private static final String PATH_SORT_TOP_RATED = "movie/top_rated";
    private static final String PATH_MOVIE = "/movie";
    private static final String PATH_MOVIE_TRAILERS = "/videos";
    private static final String PATH_MOVIE_REVIEWS = "/reviews";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PATH_IMAGES = "https://image.tmdb.org/t/p/w185";
    private final Gson mGson = new Gson();

    public static String getPosterPath(String id) {
        return PATH_IMAGES + id;
    }

    public TmdbMoviesResponse getMoviesByPopularity() {
        return fetchMovies(PATH_SORT_POPULAR);
    }

    public TmdbMoviesResponse getMoviesByRating() {
        return fetchMovies(PATH_SORT_TOP_RATED);
    }

    @Nullable
    private TmdbMoviesResponse fetchMovies(String path) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[] {path}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response == null ? null : mGson.fromJson(response, TmdbMoviesResponse.class);
    }

    @Nullable
    private List<TmdbMovieTrailer> fetchMovieTrailers(int id) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[] {
                    PATH_MOVIE,
                    String.valueOf(id),
                    PATH_MOVIE_TRAILERS
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response==null) return null;
        TmdbMovieTrailersResponse trailersResponse = mGson.fromJson(response, TmdbMovieTrailersResponse.class);
        if(trailersResponse==null) return null;
        return trailersResponse.getResults();
    }

    @Nullable
    private List<TmdbMovieReview> fetchMovieReviews(int id) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[] {
                    PATH_MOVIE,
                    String.valueOf(id),
                    PATH_MOVIE_REVIEWS
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response==null) return null;
        TmdbMovieReviewsResponse reviewsResponse = mGson.fromJson(response, TmdbMovieReviewsResponse.class);
        if(reviewsResponse==null) return null;
        return reviewsResponse.getResults();
    }

    private URL getUrl(String[] paths) {
        Uri.Builder uriBuilder = Uri
                .parse(API_URL)
                .buildUpon();
        for (String path : paths) {
            uriBuilder.appendEncodedPath(path);
        }
        uriBuilder.appendQueryParameter(PARAM_API_KEY, BuildConfig.TMDB_API_TOKEN);
        Log.d(TAG, "getUrl: " + uriBuilder.toString());
        try {
            return new URL(uriBuilder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
