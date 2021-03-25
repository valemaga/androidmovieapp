package com.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.movieapp.activities.MovieDetailActivity;
import com.android.movieapp.data.AppDatabase;
import com.android.movieapp.models.MoviePoster;
import com.android.movieapp.models.GeneralMovieResponse;
import com.android.movieapp.network.MovieService;
import com.android.movieapp.network.NetworkMapper;
import com.android.movieapp.views.adapters.MoviePosterAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.movieapp.utils.Constants.FILTER_APPLIED;
import static com.android.movieapp.utils.Constants.MOVIE_ID;


public class MainActivity extends AppCompatActivity
        implements MoviePosterAdapter.ListItemClickListener {

    //private Context context;
    private String API_KEY;
    private MovieService service;
    private List<MoviePoster> movies;
    //id identifying filter applied
    //1 stands for popular, 2 stands for ratings
    private int filterId;

    private AppDatabase mDb;

    private RecyclerView rvMovies;
    private MoviePosterAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(this);


        API_KEY = getString(R.string.api_key);

        rvMovies = (RecyclerView) findViewById(R.id.movies_recycler_view);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(this, 2);
        rvMovies.setLayoutManager(gridLayoutManager);
        movieAdapter = new MoviePosterAdapter(this, (MoviePosterAdapter.ListItemClickListener) this);
        rvMovies.setAdapter(movieAdapter);


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
                        if (filterId == 3) {
                            updateToFavorite();
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
            if (savedInstanceState.getInt(FILTER_APPLIED) == 3) {
                filterId = 3;
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
        if (filterId == 3) {
            updateToFavorite();
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
            case R.id.filter_favorite_item:
                updateToFavorite();
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {
                NetworkMapper mapper = new NetworkMapper();
                movies = mapper.mapFromEntityList(response.body().getResults());
                movieAdapter.setmMovies(movies);

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GeneralMovieResponse> call, Response<GeneralMovieResponse> response) {
                NetworkMapper mapper = new NetworkMapper();
                movies = mapper.mapFromEntityList(response.body().getResults());
                movieAdapter.setmMovies(movies);
            }
            @Override
            public void onFailure(Call<GeneralMovieResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_simple, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateToFavorite() {
        filterId = 3;
        Context context = this;
        final LiveData<List<MoviePoster>> moviesPoster = mDb.favoriteDao().getAll();
        moviesPoster.observe(this, new Observer<List<MoviePoster>>() {
            @Override
            public void onChanged(List<MoviePoster> moviePosters) {
                movies = moviePosters;
                movieAdapter.setmMovies(movies);
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