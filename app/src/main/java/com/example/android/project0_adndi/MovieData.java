package com.example.android.project0_adndi;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class MovieData implements Parcelable {

    /**
     * Método creator do parcel para aproveitarmos as funcionalidades da classe
     */
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

    // String que conecta a MainActivity com este Intent atraves de PutExtra
    static final String MOVIE_PARCEL = "movieParcel";

    private int mMovieId;
    private String mMovieName;
    private String mMovieRanking;
    private String mMoviePosterURL;
    private String mMovieLaunchDate;
    private String mMovieBackdropURL;
    private String mMovieOverView;

    /**
     * Construtor da Classe
     */
    public MovieData(int movieId, String movieName, String movieRanking, String moviePosterURL, String movieLaunchDate, String movieBackdropURL, String movieOverView) {
        this.mMovieId = movieId;
        this.mMovieName = movieName;
        this.mMovieRanking = movieRanking;
        this.mMoviePosterURL = moviePosterURL;
        this.mMovieLaunchDate = movieLaunchDate;
        this.mMovieBackdropURL = movieBackdropURL;
        this.mMovieOverView = movieOverView;
    }

    /**
     * Construtor da Classe utilizando Parcel
     */
    private MovieData(Parcel in) {
        mMovieId = in.readInt();
        mMovieName = in.readString();
        mMovieRanking = in.readString();
        mMoviePosterURL = in.readString();
        mMovieLaunchDate = in.readString();
        mMovieBackdropURL = in.readString();
        mMovieOverView = in.readString();
    }

    /**
     * Construtor da Classe vazio para Parceler
     */
    private MovieData() {
    }

    public int getMovieId() {
        return mMovieId;
    }

    String getMovieName() {
        return mMovieName;
    }

    String getMovieRanking() {
        return mMovieRanking;
    }

    String getMoviePosterURL() {
        return mMoviePosterURL;
    }

    String getMovieLaunchDate() {
        return mMovieLaunchDate;
    }

    String getMovieBackdropURL() {
        return mMovieBackdropURL;
    }

    String getMovieOverView() {
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

    /**
     * Metodo toString da classe
     *
     * @return string com a representação do MovieData
     */
    @NonNull
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
