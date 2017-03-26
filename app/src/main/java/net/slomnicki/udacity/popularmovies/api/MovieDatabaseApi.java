package net.slomnicki.udacity.popularmovies.api;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDatabaseApi {
    public static final String TAG = MovieDatabaseApi.class.getSimpleName();
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String PATH_SORT_POPULAR = "movie/popular";
    private static final String PATH_SORT_TOP_RATED = "movie/top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "8cf13ffd991cf81089bcd689b7ba5de4";
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
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response == null ? null : mGson.fromJson(response, TmdbMoviesResponse.class);
    }


    private URL getUrl(String path) {
        Uri uri = Uri
                .parse(API_URL)
                .buildUpon()
                .appendEncodedPath(path)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .build();
        Log.d(TAG, "getUrl: " + uri.toString());
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
