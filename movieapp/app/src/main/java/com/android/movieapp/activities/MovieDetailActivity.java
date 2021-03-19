package com.android.movieapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.movieapp.MovieApplication;
import com.android.movieapp.R;
import com.android.movieapp.models.MovieResponse;
import com.android.movieapp.network.MovieService;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.movieapp.utils.Constants.IMG_URL;

public class MovieDetailActivity extends AppCompatActivity {

    private MovieService service;
    private String api_key;
    private String movieId;
    private MovieResponse movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        getSupportActionBar().setHomeButtonEnabled(true);
        /*ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Intent passedIntent = getIntent();
        if(passedIntent.hasExtra("movie_id")){

            int tempMovieId = passedIntent.getIntExtra("movie_id",0);
            movieId = String.valueOf(tempMovieId);

            //API KEY saved in resources file values/secrets.xml
            api_key = getString(R.string.api_key);

            //fetches movie and customizes activity to selected movie
            setMovie();

        } else {
            Toast.makeText(this, "Error loading movie identifier", Toast.LENGTH_LONG).show();
        }


    }



    private void setMovie(){
        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<MovieResponse> movieResponse = service.getMovie(movieId, api_key);
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
                durationTextView.setText("" + movie.getRuntime() + "min");//alterar
                TextView ratingTextView = (TextView) findViewById(R.id.rating_text_view);
                ratingTextView.setText(String.valueOf(movie.getVoteAverage()) + "/10");
                TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
                descriptionTextView.setText(movie.getOverview());

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
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
}