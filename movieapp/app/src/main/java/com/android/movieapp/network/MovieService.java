package com.android.movieapp.network;


import com.android.movieapp.models.MovieResponse;
import com.android.movieapp.models.GeneralMovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("{id}")
    public Call<MovieResponse> getMovie(@Path("id") String movieId,
                                             @Query("api_key") String API_KEY);
    @GET("popular")
    public Call<GeneralMovieResponse> getPopular(@Query("api_key") String API_KEY);

    @GET("top_rated")
    public Call<GeneralMovieResponse> getRated(@Query("api_key") String API_KEY);

}
