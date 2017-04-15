package net.slomnicki.udacity.popularmovies.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.slomnicki.udacity.popularmovies.R;

class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind("Trailer " + position);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {
        private TextView mDescriptionTextView;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_trailer_description);
        }

        public void bind(String description) {
            mDescriptionTextView.setText(description);
        }
    }
}
