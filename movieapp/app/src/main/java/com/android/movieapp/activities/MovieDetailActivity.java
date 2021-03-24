package com.android.movieapp.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movieapp.MovieApplication;
import com.android.movieapp.R;
import com.android.movieapp.data.AppDatabase;
import com.android.movieapp.models.Favorite;
import com.android.movieapp.models.MovieResponse;
import com.android.movieapp.models.ReviewsItem;
import com.android.movieapp.models.ReviewsResponse;
import com.android.movieapp.models.TrailerItem;
import com.android.movieapp.models.TrailersResponse;
import com.android.movieapp.network.MovieService;
import com.android.movieapp.views.adapters.ReviewAdapter;
import com.android.movieapp.views.adapters.TrailerAdapter;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.movieapp.utils.Constants.IMG_URL;
import static com.android.movieapp.utils.Constants.MOVIE_ID;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {

    private String API_KEY;
    private MovieService service;
    private String movieId;
    private MovieResponse movie;
    private List<TrailerItem> trailers;
    private List<ReviewsItem> reviews;
    private AppDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        API_KEY = getString(R.string.api_key);

        getSupportActionBar().setHomeButtonEnabled(true);

        Intent passedIntent = getIntent();
        if (passedIntent.hasExtra(MOVIE_ID)) {

            int tempMovieId = passedIntent.getIntExtra(MOVIE_ID, 0);
            movieId = String.valueOf(tempMovieId);

            //fetches movie and customizes activity to selected movie
            setMovie();

        } else {
            Toast.makeText(this, R.string.error_movie_id, Toast.LENGTH_LONG).show();
        }
    }

    private void setupFavorite() {
        //check favorite
        mDb = AppDatabase.getInstance(this);
        Favorite favorite = mDb.favoriteDao().getFavorite(movie.getId());

        if (favorite != null) {
            ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star_image_view);

            favoriteStar.setImageResource(R.drawable.ic_baseline_star_35);
            //add on click listener to unfavorite
            favoriteStar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    deleteFavorite();
                }
            });

        } else {
            //default no favorite
            ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star_image_view);

            favoriteStar.setImageResource(R.drawable.ic_baseline_star_outline_35);
            //add on click listener to favorite
            favoriteStar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //add to favorites
                    addFavorite();
                }
            });

        }
    }

    private void addFavorite() {
        mDb = AppDatabase.getInstance(this);
        Favorite favorite = new Favorite(movie.getId(), movie.getPosterPath());
        mDb.favoriteDao().insert(favorite);
        //change to filled star
        ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star_image_view);

        favoriteStar.setImageResource(R.drawable.ic_baseline_star_35);
        //add on click listener to unfavorite
        favoriteStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteFavorite();
            }
        });

    }

    private void deleteFavorite() {
        mDb = AppDatabase.getInstance(this);
        Favorite favorite = new Favorite(movie.getId(), movie.getPosterPath());
        mDb.favoriteDao().delete(favorite);

        //change to hollow star
        ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star_image_view);

        favoriteStar.setImageResource(R.drawable.ic_baseline_star_outline_35);
        //add on click listener to favorite
        favoriteStar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //add to favorites
                addFavorite();
            }
        });

    }


    private void setMovie() {
        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<MovieResponse> movieResponse = service.getMovie(movieId, API_KEY);
        movieResponse.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movie = response.body();

                TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
                titleTextView.setText(movie.getTitle());
                ImageView posterImageView = (ImageView) findViewById(R.id.movie_detail_image_view);
                Glide.with(context).load(IMG_URL + "w185" + movie.getPosterPath()).into(posterImageView);
                TextView yearTextView = (TextView) findViewById(R.id.year_text_view);
                yearTextView.setText(movie.getReleaseDate());
                TextView durationTextView = (TextView) findViewById(R.id.duration_text_view);
                String minutes = getString(R.string.minutes);
                durationTextView.setText(movie.getRuntime() + " " + minutes);
                TextView ratingTextView = (TextView) findViewById(R.id.rating_text_view);
                ratingTextView.setText(String.valueOf(movie.getVoteAverage()) + "/10");
                TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
                descriptionTextView.setText(movie.getOverview());

                Call<TrailersResponse> trailerResponse = service.getTrailers(String.valueOf(movie.getId()), API_KEY);
                trailerResponse.enqueue(new Callback<TrailersResponse>() {
                    @Override
                    public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {

                        trailers = response.body().getResults();
                        RecyclerView rvTrailers = (RecyclerView) findViewById(R.id.trailers_recycler_view);
                        TrailerAdapter adapter = new TrailerAdapter(trailers, (TrailerAdapter.ListItemClickListener) context);
                        rvTrailers.setAdapter(adapter);

                        LinearLayoutManager linearLayoutManager =
                                new LinearLayoutManager(context);

                        rvTrailers.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onFailure(Call<TrailersResponse> call, Throwable t) {
                        Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
                    }
                });

                Call<ReviewsResponse> reviewResponse = service.getReviews(String.valueOf(movie.getId()), API_KEY);
                reviewResponse.enqueue(new Callback<ReviewsResponse>() {
                    @Override
                    public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {

                        reviews = response.body().getResults();
                        RecyclerView rvReviews = (RecyclerView) findViewById(R.id.reviews_recycler_view);
                        ReviewAdapter adapter = new ReviewAdapter(reviews);
                        rvReviews.setAdapter(adapter);
                        LinearLayoutManager linearLayoutManager =
                                new LinearLayoutManager(context);

                        rvReviews.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                        Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
                    }
                });
                //setup favorite state based on the movie
                setupFavorite();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListItemClick(int position) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailers.get(position).getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(position).getKey()));
        try {
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            this.startActivity(webIntent);
        }
    }
}