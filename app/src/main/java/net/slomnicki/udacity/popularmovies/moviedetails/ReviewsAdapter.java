package net.slomnicki.udacity.popularmovies.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.TmdbMovieReview;

import java.util.List;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<TmdbMovieReview> mReviews;

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind(mReviews.get(position));

    }

    @Override
    public int getItemCount() {
        if (mReviews == null) return 0;
        return mReviews.size();
    }

    public void swapData(List<TmdbMovieReview> reviewList) {
        mReviews = reviewList;
        notifyDataSetChanged();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private TextView mDescriptionTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_trailer_description);
        }

        public void bind(TmdbMovieReview review) {
            mDescriptionTextView.setText(review.getContent());
        }
    }
}
