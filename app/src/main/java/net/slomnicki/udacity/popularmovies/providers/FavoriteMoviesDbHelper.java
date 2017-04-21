package net.slomnicki.udacity.popularmovies.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.slomnicki.udacity.popularmovies.providers.FavoriteMoviesContract.FavoriteMovieEntry;

public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE =
                "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" +
                        FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                        FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " VARCHAR(100) NOT NULL" +
                        ")";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
