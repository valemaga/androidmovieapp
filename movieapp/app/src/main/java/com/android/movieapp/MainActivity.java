package com.android.movieapp;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.movieapp.models.PopularResponse;
import com.android.movieapp.network.MovieService;
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


        ImageView imgView = (ImageView) findViewById(R.id.iv_movie);
        Context context = this;


        String api_key = getString(R.string.api_key);


        service = ((MovieApplication) getApplication()).getService();

        Call<PopularResponse> PopularResponse = service.getPopular(api_key);
        PopularResponse.enqueue(new Callback<com.android.movieapp.models.PopularResponse>() {
            @Override
            public void onResponse(Call<com.android.movieapp.models.PopularResponse> call, Response<com.android.movieapp.models.PopularResponse> response) {
                Glide.with(context).load(IMG_URL + "w154" + response.body().getResults().get(5).getPosterPath()).into(imgView);

            }

            @Override
            public void onFailure(Call<com.android.movieapp.models.PopularResponse> call, Throwable t) {
                Toast.makeText(context, "ERRO", Toast.LENGTH_SHORT).show();
            }
        });


    }
}