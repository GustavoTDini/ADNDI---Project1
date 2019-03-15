package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "favorites")
@ForeignKey(entity = MovieData.class, parentColumns = "movie_id", childColumns = "movie_id")
public class FavoriteMovies {

    @PrimaryKey(autoGenerate = true)
    private int mFavoriteId;
    @ColumnInfo(name = "movie_id")
    private int mMovieId;

    @Ignore
    public FavoriteMovies(int movieId) {
        this.mMovieId = movieId;
    }

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

    @Override
    public String toString() {
        return "FavoriteMovies{" +
                "mFavoriteId=" + mFavoriteId +
                ", mMovieId=" + mMovieId +
                '}';
    }
}
