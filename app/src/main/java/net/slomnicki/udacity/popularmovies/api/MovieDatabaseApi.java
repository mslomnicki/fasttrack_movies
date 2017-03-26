package net.slomnicki.udacity.popularmovies.api;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieDatabaseApi {
    public static final String TAG = MovieDatabaseApi.class.getSimpleName();
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String PATH_SORT_POPULAR = "/movie/popular";
    private static final String PATH_SORT_TOP_RATED = "/movie/top_rated";
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = "8cf13ffd991cf81089bcd689b7ba5de4";
    private static final String PATH_IMAGES = "https://image.tmdb.org/t/p/w185";
    private final OkHttpClient mClient = new OkHttpClient();
    private final Gson mGson = new Gson();

    public static String getPosterPath(String id) {
        return PATH_IMAGES + id;
    }

    public TmdbMoviesResponse getMoviesByPopularity() {
        Request request = new Request.Builder()
                .url(getUrl(PATH_SORT_POPULAR))
                .build();
        Log.d(TAG, "getMoviesByPopularity: request:" + request.toString());
        try {
            Response response = mClient.newCall(request).execute();
            if(!response.isSuccessful()) return null;
            String responseBody = response.body().string();
            TmdbMoviesResponse movies = mGson.fromJson(responseBody, TmdbMoviesResponse.class);
            return movies;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TmdbMoviesResponse getMoviesByRating() {
        return null;
    }

    private HttpUrl getUrl(String path) {
        return new HttpUrl.Builder()
                .addPathSegment(API_URL)
                .addPathSegment(path)
                .addQueryParameter(PARAM_API_KEY, API_KEY)
                .build();
    }
}
