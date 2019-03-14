package com.example.android.project0_adndi.DataUtilities;

public class MovieVideos {

    private int mMovieId;
    private String mVideoTitle;
    private String mVideoKey;

    public MovieVideos(int movieId, String videoTitle, String videoKey) {
        this.mMovieId = movieId;
        this.mVideoTitle = videoTitle;
        this.mVideoKey = videoKey;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int mMovieId) {
        this.mMovieId = mMovieId;
    }

    public String getVideoTitle() {
        return mVideoTitle;
    }

    public void setVideoTitle(String mVideoTitle) {
        this.mVideoTitle = mVideoTitle;
    }

    public String getVideoKey() {
        return mVideoKey;
    }

    public void setVideoKey(String mVideoKey) {
        this.mVideoKey = mVideoKey;
    }

    @Override
    public String toString() {
        return "MovieVideos{" +
                "MovieId=" + mMovieId +
                ", VideoTitle='" + mVideoTitle + '\'' +
                ", VideoKey='" + mVideoKey + '\'' +
                '}';
    }
}
