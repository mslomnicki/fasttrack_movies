package net.slomnicki.udacity.popularmovies.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMoviesUtil {

    public static boolean isFavoriteMovie(Context context, TmdbMovie movie) {
        Cursor cursor = context.getContentResolver().query(
                FavoriteMovieEntry.CONTENT_URI,
                new String[]{
                        FavoriteMovieEntry._ID
                },
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{
                        Integer.valueOf(movie.getId()).toString()
                },
                null);
        int itemsCount = cursor.getCount();
        cursor.close();
        return itemsCount > 0;
    }

    public static void markAsFavorite(Context context, TmdbMovie movie) {
        if (isFavoriteMovie(context, movie)) return;
        ContentValues insertValues = new ContentValues();
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        insertValues.put(FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        context.getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, insertValues);
    }

    public static void unmarkAsFavorite(Context context, TmdbMovie movie) {
        context.getContentResolver().delete(
                FavoriteMovieEntry.CONTENT_URI,
                FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{
                        Integer.valueOf(movie.getId()).toString()
                });
    }

    public static List<TmdbMovie> getFavoriteMovies(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(FavoriteMovieEntry.CONTENT_URI,
                null,
                null,
                null,
                FavoriteMovieEntry._ID);
        if (!cursor.moveToFirst()) return null;
        List<TmdbMovie> movieList = new ArrayList<>();
        do {
            movieList.add(new TmdbMovie(
                    cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_ID)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_TITLE)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_POSTER_PATH)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_RELEASE_DATE)),
                    cursor.getDouble(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_MOVIE_VOTE_AVERAGE))
            ));
        } while (cursor.moveToNext());
        return movieList;
    }

}
