package com.sanvalero.android.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PersonFav {
    @PrimaryKey(autoGenerate = false)
    private Long id;
    @ColumnInfo
    private boolean favourite;

    public PersonFav(Long id, boolean favourite) {
        this.id = id;
        this.favourite = favourite;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
