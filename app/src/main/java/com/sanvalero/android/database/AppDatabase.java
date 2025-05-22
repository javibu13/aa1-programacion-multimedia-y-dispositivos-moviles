package com.sanvalero.android.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.sanvalero.android.dao.PersonFavDao;
import com.sanvalero.android.dao.PlaceFavDao;

@Database(entities = {PersonFav.class, PlaceFav.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonFavDao personFavDao();

    public abstract PlaceFavDao placeFavDao();
}