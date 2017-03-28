package net.slomnicki.udacity.popularmovies.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;

public class DetailsActivity extends AppCompatActivity {
    private static final String INTENT_MOVIE = DetailsActivity.class.getName() + "_MOVIE";

    public static void startActivity(Context context, TmdbMovie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(INTENT_MOVIE, movie);
        context.startActivity(intent);
    }

    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mUserRating;
    private TextView mOverview;
    private TmdbMovie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initializeLayoutFields();

        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle == null ||
                extrasBundle.isEmpty() ||
                !extrasBundle.containsKey(INTENT_MOVIE)) {
            finish();
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            return;
        }
        mMovie = extrasBundle.getParcelable(INTENT_MOVIE);
        fillFieldsWithMovieData();
    }

    private void initializeLayoutFields() {
        mTitle = (TextView) findViewById(R.id.tv_title);
        mPoster = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date);
        mUserRating = (TextView) findViewById(R.id.tv_user_rating);
        mOverview = (TextView) findViewById(R.id.tv_overview);
    }

    private void fillFieldsWithMovieData() {
        mTitle.setText((mMovie.getTitle()));
        Picasso
                .with(this)
                .load(MovieDatabaseApi.getPosterPath(mMovie.getPosterPath()))
                .into(mPoster);
        mReleaseDate.setText(mMovie.getReleaseDate().substring(0, 4));
        mUserRating.setText(mMovie.getVoteAverage() + "/10");
        mOverview.setText(mMovie.getOverview());
    }
}
