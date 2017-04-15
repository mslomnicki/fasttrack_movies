package net.slomnicki.udacity.popularmovies.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;

class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind("Review " + position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private RatingBar mRatingBar;
        private TextView mDescriptionTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_trailer_description);
        }

        public void bind(String description) {
            mDescriptionTextView.setText(description);
        }
    }
}
