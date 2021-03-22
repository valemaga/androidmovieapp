package com.android.movieapp;

import android.app.Application;
import android.content.Context;

import com.android.movieapp.network.MovieService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.movieapp.utils.Constants.BASE_URL;

public class MovieApplication extends Application {

    private MovieService service;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(MovieService.class);
    }

    public static Context getContext(){
        return mContext;
    }

    public MovieService getService(){
        return service;
    }
}
