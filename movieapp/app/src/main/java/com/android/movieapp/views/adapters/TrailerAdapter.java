package com.android.movieapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movieapp.MovieApplication;
import com.android.movieapp.R;
import com.android.movieapp.models.TrailerItem;

import java.util.List;

public class TrailerAdapter extends
        RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<TrailerItem> mTrailers;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int position);

    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView trailerTextView;
        ListItemClickListener onItemListener;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, ListItemClickListener onItemListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            this.onItemListener = onItemListener;
            trailerTextView = (TextView) itemView.findViewById(R.id.trailer_number_text_view);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }


    // Pass in the contact array into the constructor
    public TrailerAdapter(List<TrailerItem> trailers, ListItemClickListener listener) {
        this.mTrailers = trailers;
        this.mOnClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View trailerView = inflater.inflate(R.layout.item_trailer, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(trailerView, mOnClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {
        /*// Get the data model based on position
        TrailerItem trailerItem = mTrailers.get(position);*/
        // Set item views based on your views and data model
        TextView trailerView = holder.trailerTextView;
        int trailerIndex = position + 1;
        Context context = MovieApplication.getContext();

        trailerView.setText(context.getResources().getString(R.string.trailer_number) + trailerIndex);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }
}





