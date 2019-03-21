package com.example.android.project0_adndi.DataUtilities;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovie(MovieData movie);

    @Query("DELETE FROM movieList")
    void emptyMovieCache();

    @Query("SELECT * FROM movieList WHERE movie_id = :movieId")
    MovieData loadMovieById(int movieId);

    @Query("SELECT * FROM movieList")
    List<MovieData> loadMovies();

    @Query("SELECT * FROM movieList INNER JOIN favorites WHERE favorite = 1 AND movie_id = favorite_movie_id")
    LiveData<List<MovieData>> loadFavoriteMovies();

}