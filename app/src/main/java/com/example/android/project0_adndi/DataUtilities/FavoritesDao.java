package com.example.android.project0_adndi.DataUtilities;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Insert
    void addNewFavorite(FavoriteMovies movie);

    @Update
    void updateFavorite(FavoriteMovies movie);

    @Query("DELETE FROM favorites")
    void emptyFavorites();

    @Query("SELECT * FROM favorites WHERE favorite = 1")
    List<FavoriteMovies> loadFavorites();

    @Query("SELECT * FROM favorites WHERE movie_id = :movieId")
    LiveData<FavoriteMovies> loadFavoriteById(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE movie_id = :movieId)")
    Boolean checkIfFavoriteExists(int movieId);
}