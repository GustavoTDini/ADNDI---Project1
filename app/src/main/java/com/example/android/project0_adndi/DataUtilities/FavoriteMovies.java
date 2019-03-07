package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteMovies {

    @PrimaryKey(autoGenerate = true)
    private int mFavoriteId;
    private int mMovieId;

    public FavoriteMovies(int favoriteId, int movieId) {
        this.mFavoriteId = favoriteId;
        this.mMovieId = movieId;
    }

    public int getFavoriteId() {
        return mFavoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.mFavoriteId = favoriteId;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }
}
