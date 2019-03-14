package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "urlList")
public class UrlMovieList {

    @PrimaryKey(autoGenerate = true)
    private int mRequestId;
    @ColumnInfo(name = "request_url")
    private String mRequestUrl;
    @ColumnInfo(name = "request_page")
    private int mRequestPage;
    private int mRequestPagesTotal;
    private String mMoviesList;

    @Ignore
    public UrlMovieList(String requestURL, int requestPage, int requestPagesTotal, String moviesList) {
        this.mRequestUrl = requestURL;
        this.mRequestPage = requestPage;
        this.mRequestPagesTotal = requestPagesTotal;
        this.mMoviesList = moviesList;
    }

    public UrlMovieList(int requestId, String requestUrl, int requestPage, int requestPagesTotal, String moviesList) {
        this.mRequestId = requestId;
        this.mRequestUrl = requestUrl;
        this.mRequestPage = requestPage;
        this.mRequestPagesTotal = requestPagesTotal;
        this.mMoviesList = moviesList;
    }

    public int getRequestId() {
        return mRequestId;
    }

    public void setRequestId(int mRequestId) {
        this.mRequestId = mRequestId;
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public void setRequestUrl(String mRequestUrl) {
        this.mRequestUrl = mRequestUrl;
    }

    public int getRequestPage() {
        return mRequestPage;
    }

    public void setRequestPage(int mRequestPage) {
        this.mRequestPage = mRequestPage;
    }

    public int getRequestPagesTotal() {
        return mRequestPagesTotal;
    }

    public void setRequestPagesTotal(int mRequestPagesTotal) {
        this.mRequestPagesTotal = mRequestPagesTotal;
    }

    public String getMoviesList() {
        return mMoviesList;
    }

    public void setMoviesList(String mMoviesList) {
        this.mMoviesList = mMoviesList;
    }

    @Override
    public String toString() {
        return "UrlMovieList{" +
                "mRequestId=" + mRequestId +
                ", mRequestUrl='" + mRequestUrl + '\'' +
                ", mRequestPage=" + mRequestPage +
                ", mRequestPagesTotal=" + mRequestPagesTotal +
                ", mMoviesList='" + mMoviesList + '\'' +
                '}';
    }
}
