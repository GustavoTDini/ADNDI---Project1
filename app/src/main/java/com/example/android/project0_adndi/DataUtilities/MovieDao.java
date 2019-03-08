package com.example.android.project0_adndi.DataUtilities;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    void addMovie(MovieData Movie);

    @Insert
    void addUrlInfo(UrlMovieList Url);

    @Insert
    void addNewFavorite(FavoriteMovies Movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieData Movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUrlInfo(UrlMovieList urlRequest);

    @Delete
    void removeFavorite(FavoriteMovies Movie);

    @Query("DELETE FROM movieList")
    void emptyMovieCache();

    @Query("DELETE FROM urlList")
    void emptyUrlListCache();

    @Query("SELECT * FROM movieList WHERE movie_id = :movieId")
    LiveData<MovieData> loadMovieById(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM movieList WHERE movie_id = :movieId)")
    boolean checkIfMovieExists(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM urlList WHERE request_url = :requestUrl AND request_page = :page)")
    LiveData<UrlMovieList> checkIfRequestExists(String requestUrl, int page);

    @Query("SELECT * FROM urlList WHERE request_url = :requestUrl AND request_page = :page")
    LiveData<UrlMovieList> loadMovieUrlList(String requestUrl, int page);

    @Query("SELECT * FROM movieList")
    LiveData<List<MovieData>> updateMovieDb();

    @Query("SELECT * FROM favorites")
    LiveData<List<FavoriteMovies>> loadFavorites();

    @Query("SELECT * FROM urlList")
    LiveData<List<UrlMovieList>> updateMovieUrls();

}