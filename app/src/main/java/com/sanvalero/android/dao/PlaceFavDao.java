package com.sanvalero.android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sanvalero.android.database.PersonFav;
import com.sanvalero.android.database.PlaceFav;

import java.util.List;

@Dao
public interface PlaceFavDao {
    @Query("SELECT * FROM PlaceFav")
    List<PlaceFav> getAll();

    @Query("SELECT * FROM PlaceFav WHERE id = :id")
    PlaceFav findById(Long id);

    @Query("SELECT * FROM PlaceFav WHERE favourite = :favourite")
    List<PlaceFav> findByFavourite(boolean favourite);

    @Insert
    void insert(PlaceFav placeFav);

    @Update
    void update(PlaceFav placeFav);

    @Delete
    void delete(PlaceFav placeFav);

    @Query("DELETE FROM PlaceFav WHERE id = :id")
    void deleteById(Long id);

    @Query("DELETE FROM PlaceFav WHERE favourite = :favourite")
    void deleteByFavourite(boolean favourite);

    @Query("DELETE FROM PlaceFav")
    void deleteAll();

}
