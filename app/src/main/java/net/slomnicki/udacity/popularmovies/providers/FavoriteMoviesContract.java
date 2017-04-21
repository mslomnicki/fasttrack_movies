package net.slomnicki.udacity.popularmovies.providers;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {
    public static final String CONTENT_AUTHORITY = "net.slomnicki.udacity.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite";

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "tmdbId";
        public static final String COLUMN_MOVIE_TITLE = "title";
    }
}
