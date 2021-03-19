package com.android.movieapp.utils;

import com.android.movieapp.R;

public class Constants {

    //You can change to your personal API KEY from themoviedb.org directly here or add an xml named
    // secrets.xml with this string resource
    //API KEY saved in resources file values/secrets.xml:
    public static final String API_KEY = String.valueOf(R.string.api_key);

    public static final String BASE_URL ="https://api.themoviedb.org/3/movie/";
    public static final String IMG_URL = "http://image.tmdb.org/t/p/";
    public static final String MOVIE_ID = "movie_id";
    public static final String FILTER_APPLIED = "filter_applied";
}
