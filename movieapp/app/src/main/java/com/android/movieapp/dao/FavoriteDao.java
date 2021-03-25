package com.android.movieapp.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.movieapp.models.MoviePoster;

import java.util.List;


@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM MoviePoster")
    LiveData<List<MoviePoster>> getAll();


    @Query("SELECT * FROM MoviePoster WHERE id = :movieId")
    MoviePoster getFavorite(int movieId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(MoviePoster moviePoster);

    @Insert
    void insert(MoviePoster moviePoster);

    @Delete
    void delete(MoviePoster moviePoster);
}

