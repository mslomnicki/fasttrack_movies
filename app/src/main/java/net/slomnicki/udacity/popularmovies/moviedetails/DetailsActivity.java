package net.slomnicki.udacity.popularmovies.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieTrailer;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClickListener {
    private static final String INTENT_MOVIE = DetailsActivity.class.getName() + "_MOVIE";
    private static final int TRAILERS_LOADER_ID = 5545;
    private static final int REVIEWS_LOADER_ID = 4454;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;
    private TextView mOverviewTextView;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private TmdbMovie mMovie;

    public static void startActivity(Context context, TmdbMovie movie) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(INTENT_MOVIE, movie);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle == null ||
                extrasBundle.isEmpty() ||
                !extrasBundle.containsKey(INTENT_MOVIE)) {
            finish();
            Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
            return;
        }
        mMovie = extrasBundle.getParcelable(INTENT_MOVIE);
        initializeLayoutFields();
        initializeRecyclerViews();
        fillFieldsWithMovieData();
        initializeLoaders();
    }

    private void initializeLoaders() {
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.initLoader(TRAILERS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<TmdbMovieTrailer>>() {
            @Override
            public Loader<List<TmdbMovieTrailer>> onCreateLoader(int id, Bundle args) {
                Log.d(TAG, "onCreateLoader: trailers");
                return new TrailersLoader(DetailsActivity.this, mMovie.getId());
            }

            @Override
            public void onLoadFinished(Loader<List<TmdbMovieTrailer>> loader, List<TmdbMovieTrailer> data) {
                Log.d(TAG, "onLoadFinished: trailers " + data.size());
                mTrailersAdapter.swapData(data);
            }

            @Override
            public void onLoaderReset(Loader<List<TmdbMovieTrailer>> loader) {
                mTrailersAdapter.swapData(null);
            }
        });
    }

    private void initializeLayoutFields() {
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
    }

    private void initializeRecyclerViews() {
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        DividerItemDecoration trailersDivier = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mTrailersRecyclerView.addItemDecoration(trailersDivier);

        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        DividerItemDecoration reviewsDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mReviewsRecyclerView.addItemDecoration(reviewsDivider);
    }

    private void fillFieldsWithMovieData() {
        getSupportActionBar().setTitle(mMovie.getTitle());
        mTitleTextView.setText(mMovie.getTitle());
        Picasso
                .with(this)
                .load(MovieDatabaseApi.getPosterPath(mMovie.getPosterPath()))
                .into(mPosterImageView);
        mReleaseDateTextView.setText(mMovie.getReleaseDate().substring(0, 4));
        mUserRatingTextView.setText(mMovie.getVoteAverage() + "/10");
        mOverviewTextView.setText(mMovie.getOverview());

    }

    @Override
    public void onTrailerClick(TmdbMovieTrailer trailer) {
        Uri trailerUri = MovieDatabaseApi.getTrailerUri(this, trailer);
        Log.d(TAG, "onTrailerClick: " + trailerUri);
        if (trailerUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
            startActivity(intent);
        }
    }
}