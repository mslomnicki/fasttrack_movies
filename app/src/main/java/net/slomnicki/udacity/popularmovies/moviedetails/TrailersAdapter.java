package net.slomnicki.udacity.popularmovies.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieTrailer;

import java.util.List;

class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private List<TmdbMovieTrailer> mTrailers;
    private OnTrailerClickListener mTrailerClickListener;

    public TrailersAdapter(OnTrailerClickListener trailerClickListener) {
        mTrailerClickListener = trailerClickListener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(mTrailers.get(position));

    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) return 0;
        return mTrailers.size();
    }

    public void swapData(List<TmdbMovieTrailer> data) {
        mTrailers = data;
        notifyDataSetChanged();
    }

    public interface OnTrailerClickListener {
        void onTrailerClick(TmdbMovieTrailer trailer);
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mDescriptionTextView;
        private TmdbMovieTrailer mTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_trailer_description);
            itemView.setOnClickListener(this);
        }

        public void bind(TmdbMovieTrailer trailer) {
            mTrailer = trailer;
            mDescriptionTextView.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            if (mTrailerClickListener != null) mTrailerClickListener.onTrailerClick(mTrailer);
        }
    }
}
