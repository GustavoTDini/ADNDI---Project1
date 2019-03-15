package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface MovieDao {

    @Insert
    void addMovie(MovieData movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieData movie);

    @Query("DELETE FROM movieList")
    void emptyMovieCache();

    @Query("SELECT * FROM movieList WHERE movie_id = :movieId")
    MovieData loadMovieById(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM movieList WHERE movie_id = :movieId)")
    boolean checkIfMovieExists(int movieId);

}