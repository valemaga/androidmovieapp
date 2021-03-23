package com.android.movieapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movieapp.MovieApplication;
import com.android.movieapp.R;
import com.android.movieapp.models.ReviewsItem;
import com.android.movieapp.models.TrailerItem;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<ReviewsItem> mReviews;

    public interface ListItemClickListener {
        void onListItemClick(int position);

    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView usernameTextView;
        ImageView avatarImageView;
        TextView contentTextView;
        TextView ratingTextView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.review_username_text_view);
            avatarImageView = (ImageView) itemView.findViewById(R.id.review_avatar_image_view);
            contentTextView = (TextView)  itemView.findViewById(R.id.review_content_text_view);
            ratingTextView = (TextView)  itemView.findViewById(R.id.review_rating_text_view);

        }



    }


    // Pass in the contact array into the constructor
    public ReviewAdapter(List<ReviewsItem> reviews) {
        this.mReviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View reviewView = inflater.inflate(R.layout.item_review, parent, false);

        // Return a new holder instance
        ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        ReviewsItem reviewItem = mReviews.get(position);
        // Set item views based on your views and data model
        if(reviewItem.getAuthor() != null){
            TextView username = holder.usernameTextView;
            username.setText(reviewItem.getAuthor());
        }
        if(reviewItem.getContent() != null){
            TextView content = holder.contentTextView;
            content.setText(reviewItem.getContent());
        }
        if(reviewItem.getAuthorDetails().getRating() != null){
            TextView rating = holder.ratingTextView;
            rating.setText(String.valueOf(reviewItem.getAuthorDetails().getRating()));
        }
        //set avatar image

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }
}

