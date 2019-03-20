package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UrlDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUrlInfo(UrlMovieList url);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUrlInfo(UrlMovieList url);

    @Query("DELETE FROM urlList")
    void emptyUrlListCache();

    @Query("SELECT * FROM urlList WHERE request_url = :requestUrl AND request_page = :page")
    UrlMovieList loadMovieUrlList(String requestUrl, int page);

    @Query("SELECT * FROM urlList")
    List<UrlMovieList> loadUrlList();

    @Query("SELECT EXISTS(SELECT 1 FROM urlList WHERE request_url = :requestUrl AND request_page = :page)")
    boolean checkIfRequestExists(String requestUrl, int page);

}