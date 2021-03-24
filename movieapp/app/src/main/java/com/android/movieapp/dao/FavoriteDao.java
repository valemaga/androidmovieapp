package com.android.movieapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.android.movieapp.models.Favorite;

import java.util.List;


@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM Favorite")
    List<Favorite> getAll();


    @Query("SELECT * FROM Favorite WHERE id = :movieId")
    Favorite getFavorite(int movieId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(Favorite favorite);

    @Insert
    void insert(Favorite favorite);

    @Delete
    void delete(Favorite favorite);
}

