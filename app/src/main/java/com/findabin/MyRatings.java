package com.findabin;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.findabin.placeholder.PlaceholderContent.PlaceholderItem;
import com.findabin.databinding.FragmentRatingsBinding;

import java.util.List;
import java.util.Random;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyRatings extends RecyclerView.Adapter<MyRatings.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public MyRatings(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentRatingsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Random r = new Random();
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).content);
        holder.mContentView.setText(mValues.get(position).details);
        holder.mRatingBar.setRating(r.nextInt(5)+1);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public final RatingBar mRatingBar;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentRatingsBinding binding) {
            super(binding.getRoot());
            mIdView = binding.binLocation;
            mContentView = binding.content;
            mRatingBar = binding.ratingBar;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}