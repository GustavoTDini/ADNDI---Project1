package com.example.android.project0_adndi;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieData implements Parcelable {

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
    private int mMovieId;
    private String mMovieName;
    private String mMovieRanking;
    private String mMoviePosterURL;
    private String mMovieLaunchDate;
    private String mMovieBackdropURL;
    private String mMovieOverView;

    public MovieData(int movieId, String movieName, String movieRanking, String moviePosterURL, String movieLaunchDate, String movieBackdropURL, String movieOverView) {
        this.mMovieId = movieId;
        this.mMovieName = movieName;
        this.mMovieRanking = movieRanking;
        this.mMoviePosterURL = moviePosterURL;
        this.mMovieLaunchDate = movieLaunchDate;
        this.mMovieBackdropURL = movieBackdropURL;
        this.mMovieOverView = movieOverView;
    }

    private MovieData(Parcel in) {
        mMovieId = in.readInt();
        mMovieName = in.readString();
        mMovieRanking = in.readString();
        mMoviePosterURL = in.readString();
        mMovieLaunchDate = in.readString();
        mMovieBackdropURL = in.readString();
        mMovieOverView = in.readString();
    }

    public int getMovieId() {
        return mMovieId;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public String getMovieRanking() {
        return mMovieRanking;
    }

    public String getMoviePosterURL() {
        return mMoviePosterURL;
    }

    public String getMovieLaunchDate() {
        return mMovieLaunchDate;
    }

    public String getMovieBackdropURL() {
        return mMovieBackdropURL;
    }

    public String getMovieOverView() {
        return mMovieOverView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mMovieId);
        parcel.writeString(mMovieName);
        parcel.writeString(mMovieRanking);
        parcel.writeString(mMoviePosterURL);
        parcel.writeString(mMovieLaunchDate);
        parcel.writeString(mMovieBackdropURL);
        parcel.writeString(mMovieOverView);


    }

    @Override
    public String toString() {
        return "MovieData{" +
                "mMovieId='" + mMovieId + '\'' +
                "mMovieName='" + mMovieName + '\'' +
                ", mMovieRanking='" + mMovieRanking + '\'' +
                ", mMoviePosterURL='" + mMoviePosterURL + '\'' +
                ", mMovieLaunchDate='" + mMovieLaunchDate + '\'' +
                ", mMovieBackgroundURL='" + mMovieBackdropURL + '\'' +
                ", mMovieOverView='" + mMovieOverView + '\'' +
                '}';
    }
}
