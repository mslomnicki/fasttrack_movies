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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieReview;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieTrailer;
import net.slomnicki.udacity.popularmovies.providers.FavoriteMoviesUtil;

import java.util.List;

public class DetailsActivity extends AppCompatActivity implements TrailersAdapter.OnTrailerClickListener {
    private static final String INTENT_MOVIE = DetailsActivity.class.getName() + "_MOVIE";
    private static final int TRAILERS_LOADER_ID = 5545;
    private static final int REVIEWS_LOADER_ID = 4454;
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
    private TextView mReviewsTextView;
    private TextView mTrailersTextView;
    private Button mFavoriteButton;
    private ImageView mFavoriteImageView;

    private TmdbMovieTrailer mFirstTrailer;

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

    private void initializeLayoutFields() {
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mTrailersTextView = (TextView) findViewById(R.id.tv_trailers);
        mReviewsTextView = (TextView) findViewById(R.id.tv_reviews);
        mFavoriteButton = (Button) findViewById(R.id.bn_mark_favorite);
        mFavoriteImageView = (ImageView) findViewById(R.id.iv_favorite);
    }

    private void initializeRecyclerViews() {
        initializeTrailersRecyclerView();
        initializeReviewsRecyclerView();
    }

    private void initializeTrailersRecyclerView() {
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        DividerItemDecoration trailersDivier = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mTrailersRecyclerView.addItemDecoration(trailersDivier);
    }

    private void initializeReviewsRecyclerView() {
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
        mUserRatingTextView.setText(
                getString(R.string.rating, mMovie.getVoteAverage()));
        mOverviewTextView.setText(mMovie.getOverview());
        updateFavoriteState();
    }

    private void initializeLoaders() {
        LoaderManager supportLoaderManager = getSupportLoaderManager();
        supportLoaderManager.initLoader(TRAILERS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<TmdbMovieTrailer>>() {
            @Override
            public Loader<List<TmdbMovieTrailer>> onCreateLoader(int id, Bundle args) {
                return new TrailersLoader(DetailsActivity.this, mMovie.getId());
            }

            @Override
            public void onLoadFinished(Loader<List<TmdbMovieTrailer>> loader, List<TmdbMovieTrailer> data) {
                swapTrailersData(data);
            }

            @Override
            public void onLoaderReset(Loader<List<TmdbMovieTrailer>> loader) {
                swapTrailersData(null);
            }
        });
        supportLoaderManager.initLoader(REVIEWS_LOADER_ID, null, new LoaderManager.LoaderCallbacks<List<TmdbMovieReview>>() {
            @Override
            public Loader<List<TmdbMovieReview>> onCreateLoader(int id, Bundle args) {
                return new ReviewsLoader(DetailsActivity.this, mMovie.getId());
            }

            @Override
            public void onLoadFinished(Loader<List<TmdbMovieReview>> loader, List<TmdbMovieReview> data) {
                swapReviewsData(data);

            }

            @Override
            public void onLoaderReset(Loader<List<TmdbMovieReview>> loader) {
                swapReviewsData(null);

            }
        });
    }

    private void swapTrailersData(List<TmdbMovieTrailer> data) {
        mTrailersAdapter.swapData(data);
        int visibilityState = data == null || data.size() == 0 ?
                View.INVISIBLE : View.VISIBLE;
        findViewById(R.id.divider_trailers).setVisibility(visibilityState);
        mTrailersTextView.setVisibility(visibilityState);
        mTrailersRecyclerView.setVisibility(visibilityState);
        if (data != null && data.size() > 0) mFirstTrailer = data.get(0);
    }

    private void swapReviewsData(List<TmdbMovieReview> data) {
        mReviewsAdapter.swapData(data);
        int visibilityState = data == null || data.size() == 0 ?
                View.GONE : View.VISIBLE;
        findViewById(R.id.divider_reviews).setVisibility(visibilityState);
        mReviewsTextView.setVisibility(visibilityState);
        mReviewsRecyclerView.setVisibility(visibilityState);

    }

    public void onFavoriteButtonClick(View view) {
        if (FavoriteMoviesUtil.isFavoriteMovie(this, mMovie)) {
            FavoriteMoviesUtil.unmarkAsFavorite(this, mMovie);
        } else {
            FavoriteMoviesUtil.markAsFavorite(this, mMovie);
        }
        updateFavoriteState();
    }

    private void updateFavoriteState() {
        boolean favoriteMovie = FavoriteMoviesUtil.isFavoriteMovie(this, mMovie);
        mFavoriteButton.setText(favoriteMovie ?
                getString(R.string.favorite_unmark) :
                getString(R.string.favorite_mark));
        mFavoriteImageView.setImageResource(favoriteMovie ?
                R.drawable.ic_star_24dp :
                R.drawable.ic_star_border_24dp);
    }

    @Override
    public void onTrailerClick(TmdbMovieTrailer trailer) {
        Uri trailerUri = MovieDatabaseApi.getTrailerUri(this, trailer);
        if (trailerUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, trailerUri);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share && mFirstTrailer != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, MovieDatabaseApi.getTrailerUri(this, mFirstTrailer).toString());
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, null));
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
