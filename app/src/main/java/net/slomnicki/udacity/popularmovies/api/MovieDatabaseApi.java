package net.slomnicki.udacity.popularmovies.api;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import net.slomnicki.udacity.popularmovies.BuildConfig;
import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MovieDatabaseApi {
    private static final String TAG = MovieDatabaseApi.class.getSimpleName();
    private static final String API_URL = "https://api.themoviedb.org/3";
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_MOVIE_SORT_POPULAR = "popular";
    private static final String PATH_MOVIE_SORT_TOP_RATED = "top_rated";
    private static final String PATH_MOVIE_TRAILERS = "videos";
    private static final String PATH_MOVIE_REVIEWS = "reviews";
    private static final String PARAM_API_KEY = "api_key";
    private static final String PATH_IMAGES = "https://image.tmdb.org/t/p/w185";
    private final static Gson sGson = new Gson();

    public static String getPosterPath(String id) {
        return PATH_IMAGES + id;
    }

    public static Uri getTrailerUri(Context context, TmdbMovieTrailer trailer) {
        if (!trailer.getSite().equals("YouTube")) return null;
        String youtubeLink = context.getResources().getString(R.string.youtube_uri, trailer.getKey());
        return Uri.parse(youtubeLink);
    }

    public TmdbMoviesResponse getMoviesByPopularity() {
        return fetchMovies(PATH_MOVIE_SORT_POPULAR);
    }

    public TmdbMoviesResponse getMoviesByRating() {
        return fetchMovies(PATH_MOVIE_SORT_TOP_RATED);
    }

    @Nullable
    private TmdbMoviesResponse fetchMovies(String path) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[]{
                    PATH_MOVIE,
                    path}));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response == null ? null : sGson.fromJson(response, TmdbMoviesResponse.class);
    }

    @Nullable
    public List<TmdbMovieTrailer> fetchMovieTrailers(int id) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[]{
                    PATH_MOVIE,
                    String.valueOf(id),
                    PATH_MOVIE_TRAILERS
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) return null;
        TmdbMovieTrailersResponse trailersResponse = sGson.fromJson(response, TmdbMovieTrailersResponse.class);
        if (trailersResponse == null) return null;
        return trailersResponse.getResults();
    }

    @Nullable
    public List<TmdbMovieReview> fetchMovieReviews(int id) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(getUrl(new String[]{
                    PATH_MOVIE,
                    String.valueOf(id),
                    PATH_MOVIE_REVIEWS
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null) return null;
        TmdbMovieReviewsResponse reviewsResponse = sGson.fromJson(response, TmdbMovieReviewsResponse.class);
        if (reviewsResponse == null) return null;
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
