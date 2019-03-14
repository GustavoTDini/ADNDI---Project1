package com.example.android.project0_adndi.DataUtilities;

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
    void addMovie(MovieData movie);

    @Insert
    void addUrlInfo(UrlMovieList url);

    @Insert
    void addNewFavorite(FavoriteMovies movie);

    @Insert
    void addReviewsData(MovieReviews reviews);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieData movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUrlInfo(UrlMovieList urlRequest);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateReviewsInfo(MovieReviews reviews);

    @Delete
    void removeFavorite(FavoriteMovies movie);

    @Query("DELETE FROM movieList")
    void emptyMovieCache();

    @Query("DELETE FROM urlList")
    void emptyUrlListCache();

    @Query("DELETE FROM movieReviews")
    void emptyReviewsCache();

    @Query("SELECT * FROM movieList WHERE movie_id = :movieId")
    MovieData loadMovieById(int movieId);

    @Query("SELECT * FROM urlList WHERE request_url = :requestUrl AND request_page = :page")
    UrlMovieList loadMovieUrlList(String requestUrl, int page);

    @Query("SELECT * FROM movieList")
    List<MovieData> updateMovieDb();

    @Query("SELECT * FROM favorites")
    List<FavoriteMovies> loadFavorites();

    @Query("SELECT * FROM urlList")
    List<UrlMovieList> updateMovieUrls();

    @Query("SELECT EXISTS(SELECT 1 FROM movieReviews WHERE movie_id = :movieId AND reviews_author = :author)")
    boolean checkIfReviewsExists(int movieId, String author);

    @Query("SELECT EXISTS(SELECT 1 FROM movieList WHERE movie_id = :movieId)")
    boolean checkIfMovieExists(int movieId);

    @Query("SELECT EXISTS(SELECT 1 FROM urlList WHERE request_url = :requestUrl AND request_page = :page)")
    boolean checkIfRequestExists(String requestUrl, int page);



}