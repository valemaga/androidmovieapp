package com.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.movieapp.activities.MovieDetailActivity;
import com.android.movieapp.models.GeneralMovieResponse;
import com.android.movieapp.models.ResultsItem;
import com.android.movieapp.network.MovieService;
import com.android.movieapp.views.adapters.MoviePosterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.ListItemClickListener {

    private MovieService service;
    private String api_key;
    private List<ResultsItem> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API KEY saved in resources file values/secrets.xml
        api_key = getString(R.string.api_key);

        //default update of movie list
        updateToPopular();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_popular_item:
                updateToPopular();
                return true;
            case R.id.filter_rating_item:
                updateToRating();
                return true;
            case R.id.rv_movies:
                //
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void updateToPopular(){

        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<GeneralMovieResponse> popularResponse = service.getPopular(api_key);
        popularResponse.enqueue(new Callback<GeneralMovieResponse>() {
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {

                movies = response.body().getResults();
                RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
                MoviePosterAdapter adapter = new MoviePosterAdapter(movies, (MoviePosterAdapter.ListItemClickListener) context);
                rvMovies.setAdapter(adapter);
                // First param is number of columns and second param is orientation i.e Vertical or Horizontal
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(context,2);

                rvMovies.setLayoutManager(gridLayoutManager);
            }

            @Override
            public void onFailure(Call<GeneralMovieResponse> call, Throwable t) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateToRating(){
        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<GeneralMovieResponse> ratedResponse = service.getRated(api_key);
        ratedResponse.enqueue(new Callback<GeneralMovieResponse>() {
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {

                movies = response.body().getResults();
                RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
                MoviePosterAdapter adapter = new MoviePosterAdapter(movies, (MoviePosterAdapter.ListItemClickListener) context);
                rvMovies.setAdapter(adapter);

                // First param is number of columns and second param is orientation i.e Vertical or Horizontal
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(context,2);

                rvMovies.setLayoutManager(gridLayoutManager);
            }

            @Override
            public void onFailure(Call<GeneralMovieResponse> call, Throwable t) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onListItemClick(int position) {

        //intent e mudar para descrição de filme 2:50
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie_id", movies.get(position).getId());
        startActivity(intent);
    }


}