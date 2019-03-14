package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movieReviews")
public class MovieReviews {

    @PrimaryKey(autoGenerate = true)
    private int mReviewId;
    @ColumnInfo(name = "movie_id")
    private int mMovieId;
    @ColumnInfo(name = "reviews_author")
    private String mReviewAuthor;
    private String mReviewContent;

    @Ignore
    public MovieReviews(int movieId, String reviewAuthor, String reviewContent) {
        this.mMovieId = movieId;
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
    }

    public MovieReviews(int reviewId, int movieId, String reviewAuthor, String reviewContent) {
        this.mReviewId = reviewId;
        this.mMovieId = movieId;
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
    }

    public int getReviewId() {
        return mReviewId;
    }

    public void setReviewId(int mReviewId) {
        this.mReviewId = mReviewId;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getReviewAuthor() {
        return mReviewAuthor;
    }

    public void setReviewAuthor(String mReviewAuthor) {
        this.mReviewAuthor = mReviewAuthor;
    }

    public String getReviewContent() {
        return mReviewContent;
    }

    public void setReviewContent(String mReviewContent) {
        this.mReviewContent = mReviewContent;
    }

    @Override
    public String toString() {
        return "MovieReviews{" +
                "ReviewId=" + mReviewId +
                ", MovieId=" + mMovieId +
                ", ReviewAuthor='" + mReviewAuthor + '\'' +
                ", ReviewContent='" + mReviewContent + '\'' +
                '}';
    }
}
