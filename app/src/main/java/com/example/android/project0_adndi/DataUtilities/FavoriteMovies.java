package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites")
@ForeignKey(entity = MovieData.class, parentColumns = "movie_id", childColumns = "movie_id")
public class FavoriteMovies {

    @PrimaryKey
    @ColumnInfo(name = "favorite_movie_id")
    private int mMovieId;
    @ColumnInfo(name = "favorite")
    private Boolean isMovieFavorite;

    public FavoriteMovies(int movieId) {
        this.mMovieId = movieId;
        this.isMovieFavorite = false;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }

    public Boolean getMovieFavorite() {
        return isMovieFavorite;
    }

    public void setMovieFavorite(Boolean movieFavorite) {
        isMovieFavorite = movieFavorite;
    }

    @NonNull
    @Override
    public String toString() {
        return "FavoriteMovies{" +
                ", MovieId=" + mMovieId +
                ", isMovieFavorite=" + isMovieFavorite +
                '}';
    }
}
