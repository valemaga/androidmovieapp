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
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.movieapp.activities.MovieDetailActivity;
import com.android.movieapp.dao.FavoriteDao;
import com.android.movieapp.data.AppDatabase;
import com.android.movieapp.models.Favorite;
import com.android.movieapp.models.GeneralMovieItem;
import com.android.movieapp.models.GeneralMovieResponse;
import com.android.movieapp.network.MovieService;
import com.android.movieapp.views.adapters.MoviePosterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.movieapp.utils.Constants.FILTER_APPLIED;
import static com.android.movieapp.utils.Constants.MOVIE_ID;


public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.ListItemClickListener {


    private String API_KEY;
    private MovieService service;
    private List<GeneralMovieItem> movies;
    //id identifying filter applied
    //1 stands for popular, 2 stands for ratings
    private int filterId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        API_KEY = getString(R.string.api_key);
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (filterId == 1) {
                            updateToPopular();
                            if (swipeLayout.isRefreshing()) {
                                swipeLayout.setRefreshing(false);
                            }
                            return;
                        }
                        if (filterId == 2) {
                            updateToRating();
                            if (swipeLayout.isRefreshing()) {
                                swipeLayout.setRefreshing(false);
                            }
                            return;
                        }
                    }
                }
        );


        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(FILTER_APPLIED) == 1) {
                filterId = 1;
            }
            if (savedInstanceState.getInt(FILTER_APPLIED) == 2) {
                filterId = 2;
            }
        } else {
            //default filter is set to 1 (popular)
            filterId = 1;
        }
        if (filterId == 1) {
            updateToPopular();
        }
        if (filterId == 2) {
            updateToRating();
        }


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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateToPopular() {
        filterId = 1;
        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<GeneralMovieResponse> popularResponse = service.getPopular(API_KEY);
        popularResponse.enqueue(new Callback<GeneralMovieResponse>() {
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {


                movies = response.body().getResults();
                RecyclerView rvMovies = (RecyclerView) findViewById(R.id.movies_recycler_view);
                MoviePosterAdapter adapter = new MoviePosterAdapter(movies, (MoviePosterAdapter.ListItemClickListener) context);
                rvMovies.setAdapter(adapter);
                // First param is number of columns and second param is orientation i.e Vertical or Horizontal
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(context, 2);

                rvMovies.setLayoutManager(gridLayoutManager);
            }

            @Override
            public void onFailure(Call<GeneralMovieResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void updateToRating() {
        filterId = 2;
        Context context = this;

        service = ((MovieApplication) getApplication()).getService();

        Call<GeneralMovieResponse> ratedResponse = service.getRated(API_KEY);
        ratedResponse.enqueue(new Callback<GeneralMovieResponse>() {
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {

                movies = response.body().getResults();
                RecyclerView rvMovies = (RecyclerView) findViewById(R.id.movies_recycler_view);
                MoviePosterAdapter adapter = new MoviePosterAdapter(movies, (MoviePosterAdapter.ListItemClickListener) context);
                rvMovies.setAdapter(adapter);

                // First param is number of columns and second param is orientation i.e Vertical or Horizontal
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(context, 2);

                rvMovies.setLayoutManager(gridLayoutManager);
            }

            @Override
            public void onFailure(Call<GeneralMovieResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onListItemClick(int position) {

        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID, movies.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(FILTER_APPLIED, filterId);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}