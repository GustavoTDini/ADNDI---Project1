package com.example.android.project0_adndi.DataUtilities;

public class MovieReviews {

    private int mMovieId;
    private String mReviewAuthor;
    private String mReviewContent;

    public MovieReviews(int movieId, String reviewAuthor, String reviewContent) {
        this.mMovieId = movieId;
        this.mReviewAuthor = reviewAuthor;
        this.mReviewContent = reviewContent;
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
                ", MovieId=" + mMovieId +
                ", ReviewAuthor='" + mReviewAuthor + '\'' +
                ", ReviewContent='" + mReviewContent + '\'' +
                '}';
    }
}
