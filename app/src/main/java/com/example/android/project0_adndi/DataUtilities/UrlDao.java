package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface UrlDao {

    @Insert
    void addUrlInfo(UrlMovieList url);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUrlInfo(UrlMovieList urlRequest);

    @Query("DELETE FROM urlList")
    void emptyUrlListCache();

    @Query("SELECT * FROM urlList WHERE request_url = :requestUrl AND request_page = :page")
    UrlMovieList loadMovieUrlList(String requestUrl, int page);

    @Query("SELECT EXISTS(SELECT 1 FROM urlList WHERE request_url = :requestUrl AND request_page = :page)")
    boolean checkIfRequestExists(String requestUrl, int page);

}