package com.sanvalero.android.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sanvalero.android.database.PersonFav;

import java.util.List;

@Dao
public interface PersonFavDao {
    @Query("SELECT * FROM PersonFav")
    List<PersonFav> getAll();

    @Query("SELECT * FROM PersonFav WHERE id = :id")
    PersonFav findById(Long id);

    @Query("SELECT * FROM PersonFav WHERE favourite = :favourite")
    List<PersonFav> findByFavourite(boolean favourite);

    @Insert
    void insert(PersonFav personFav);

    @Update
    void update(PersonFav personFav);

    @Delete
    void delete(PersonFav personFav);

    @Query("DELETE FROM PersonFav WHERE id = :id")
    void deleteById(Long id);

    @Query("DELETE FROM PersonFav WHERE favourite = :favourite")
    void deleteByFavourite(boolean favourite);

    @Query("DELETE FROM PersonFav")
    void deleteAll();

}
