package com.android.movieapp.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.android.movieapp.dao.FavoriteDao;
import com.android.movieapp.models.MoviePoster;

@Database(entities = {MoviePoster.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movies";
    private static AppDatabase sInstance;


    public abstract FavoriteDao favoriteDao();

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating a new database");
                sInstance = Room.databaseBuilder(context,
                        AppDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting database instance");
        return sInstance;
    }

}
