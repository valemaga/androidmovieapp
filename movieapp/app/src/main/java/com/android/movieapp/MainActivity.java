package com.android.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.movieapp.models.PopularResponse;
import com.android.movieapp.network.MovieService;
import com.android.movieapp.views.adapters.MoviePosterAdapter;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.movieapp.utils.Constants.IMG_URL;


public class MainActivity extends AppCompatActivity {

    MovieService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //ImageView imgView = (ImageView) findViewById(R.id.iv_movie);
        Context context = this;


        String api_key = getString(R.string.api_key);


        service = ((MovieApplication) getApplication()).getService();

        Call<PopularResponse> PopularResponse = service.getPopular(api_key);
        PopularResponse.enqueue(new Callback<com.android.movieapp.models.PopularResponse>() {
            @Override
            public void onResponse(Call<com.android.movieapp.models.PopularResponse> call, Response<com.android.movieapp.models.PopularResponse> response) {


                RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movies);
                MoviePosterAdapter adapter = new MoviePosterAdapter(response.body().getResults());
                rvMovies.setAdapter(adapter);

                // First param is number of columns and second param is orientation i.e Vertical or Horizontal
                GridLayoutManager gridLayoutManager =
                        new GridLayoutManager(context,2);

                rvMovies.setLayoutManager(gridLayoutManager);
            }

            @Override
            public void onFailure(Call<com.android.movieapp.models.PopularResponse> call, Throwable t) {
                Toast.makeText(context, "ERRO", Toast.LENGTH_SHORT).show();
            }
        });


    }
}