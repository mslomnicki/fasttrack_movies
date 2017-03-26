package net.slomnicki.udacity.popularmovies.movieposters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApi;

class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.PosterViewHolder> {

    private final String[] mMoviesArray;
    private MovieDatabaseApi mMovieDatabaseApi;

    public PostersAdapter(MovieDatabaseApi movieDatabaseApi) {
        mMovieDatabaseApi = movieDatabaseApi;
        mMoviesArray = mMovieDatabaseApi.getMovies();
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PosterViewHolder(inflater.inflate(R.layout.item_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(mMoviesArray[position]);
    }

    @Override
    public int getItemCount() {
        return mMoviesArray.length;
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPoster;

        public PosterViewHolder(View itemView) {
            super(itemView);
            mPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        public void bind(String link) {
            Picasso
                    .with(mPoster.getContext())
                    .load(link)
                    .into(mPoster);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Yello!", Toast.LENGTH_SHORT).show();

        }
    }
}
