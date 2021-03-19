package com.android.movieapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movieapp.R;
import com.android.movieapp.models.ResultsItem;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.android.movieapp.utils.Constants.IMG_URL;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class MoviePosterAdapter extends
        RecyclerView.Adapter<MoviePosterAdapter.ViewHolder>{

    private List<ResultsItem> mMovies;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int position);

    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView movieImageView;
        ListItemClickListener onItemListener;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, ListItemClickListener onItemListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.onItemListener = onItemListener;
            movieImageView = (ImageView) itemView.findViewById(R.id.movie_image_view);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }


    // Pass in the contact array into the constructor
    public MoviePosterAdapter(List<ResultsItem> movies, ListItemClickListener listener) {
        this.mMovies = movies;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public MoviePosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(movieView, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePosterAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        ResultsItem movie = mMovies.get(position);

        // Set item views based on your views and data model
        ImageView imageView = holder.movieImageView;
        Glide.with(holder.movieImageView.getContext()).load(IMG_URL + "w185" + movie.getPosterPath()).into(imageView);

    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }
}





