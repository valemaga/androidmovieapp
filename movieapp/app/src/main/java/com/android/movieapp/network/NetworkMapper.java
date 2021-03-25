package com.android.movieapp.network;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.movieapp.models.GeneralMovieItem;
import com.android.movieapp.models.MoviePoster;
import com.android.movieapp.utils.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

public class NetworkMapper implements EntityMapper<GeneralMovieItem, MoviePoster> {


    @Override
    public MoviePoster mapFromEntity(GeneralMovieItem generalMovieItem) {
        return new MoviePoster(generalMovieItem.getId(), generalMovieItem.getPosterPath());
    }

    @Override
    public GeneralMovieItem mapToEntity(MoviePoster moviePoster) {
        GeneralMovieItem generalMovieItem = new GeneralMovieItem();
        generalMovieItem.setId(moviePoster.getId());
        generalMovieItem.setPosterPath(moviePoster.getPosterPath());
        return generalMovieItem;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<MoviePoster> mapFromEntityList(List<GeneralMovieItem> generalMovieItem){
        List<MoviePoster> moviePosters = generalMovieItem.stream()
                .map(generalMovie -> new MoviePoster(generalMovie.getId(), generalMovie.getPosterPath()))
                .collect(Collectors.toList());
        return moviePosters;
    }

}
