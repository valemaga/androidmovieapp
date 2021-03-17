package com.android.movieapp;

import android.app.Application;

import com.android.movieapp.network.MovieService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.movieapp.utils.Constants.BASE_URL;

public class MovieApplication extends Application {

    private MovieService service;

    @Override
    public void onCreate() {
        super.onCreate();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MovieService.class);
    }

    public MovieService getService(){
        return service;
    }
}
