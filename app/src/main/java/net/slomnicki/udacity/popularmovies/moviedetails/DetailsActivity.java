package net.slomnicki.udacity.popularmovies.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;

public class DetailsActivity extends AppCompatActivity {
    public static void startActivity(Context context, int movieId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, movieId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
