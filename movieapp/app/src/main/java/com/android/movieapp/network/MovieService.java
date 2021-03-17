package com.android.movieapp.network;


import com.android.movieapp.models.MovieResponse;
import com.android.movieapp.models.PopularResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("{id}")
    public Call<MovieResponse> getMovie(@Path("id") String movieId,
                                             @Query("api_key") String API_KEY);
    @GET("popular")
    public Call<PopularResponse> getPopular(@Query("api_key") String API_KEY);

}
