package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Insert
    void addNewFavorite(FavoriteMovies movie);

    @Delete
    void removeFavorite(FavoriteMovies movie);

    @Query("DELETE FROM favorites")
    void emptyFavorites();

    @Query("SELECT * FROM favorites")
    List<FavoriteMovies> loadFavorites();

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE movie_id = :movieId)")
    boolean checkIfIsFavorite(int movieId);


}