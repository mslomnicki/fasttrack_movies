package net.slomnicki.udacity.popularmovies.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class FavoriteMoviesProvider extends ContentProvider {

    public static final int CODE_FAVORITES = 100;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private FavoriteMoviesDbHelper mDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMoviesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, CODE_FAVORITES);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                SQLiteDatabase database = mDbHelper.getReadableDatabase();
                cursor = database.query(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insertedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                insertedRows = database.insert(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        null,
                        values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (insertedRows != 0) getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedRows;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITES:
                SQLiteDatabase database = mDbHelper.getWritableDatabase();
                deletedRows = database.delete(
                        FavoriteMoviesContract.FavoriteMovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deletedRows != 0) getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Not implemented");
    }
}
