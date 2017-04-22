package net.slomnicki.udacity.popularmovies.movieposters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;
import net.slomnicki.udacity.popularmovies.api.TmdbMovie;

import java.util.List;

class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.PosterViewHolder> {

    private final OnPosterClickListener mClickListener;
    private List<TmdbMovie> mMovieList;

    public PostersAdapter(OnPosterClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void swapMovieList(List<TmdbMovie> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PosterViewHolder(inflater.inflate(R.layout.item_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(PosterViewHolder viewHolder, int position) {
        viewHolder.bind(mMovieList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovieList == null ? 0 : mMovieList.size();
    }

    public interface OnPosterClickListener {
        void onPosterClick(TmdbMovie movie);
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mPoster;
        private TmdbMovie mMovie;

        public PosterViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(TmdbMovie movie) {
            mMovie = movie;
            Picasso
                    .with(mPoster.getContext())
                    .load(MovieDatabaseApi.getPosterPath(movie.getPosterPath()))
                    .into(mPoster);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onPosterClick(mMovie);
        }
    }
}

