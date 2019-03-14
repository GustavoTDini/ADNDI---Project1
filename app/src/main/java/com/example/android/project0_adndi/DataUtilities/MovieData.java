package com.example.android.project0_adndi.DataUtilities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "movieList")
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
    public static final String MOVIE_PARCEL = "movieParcel";

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    private int mMovieId;
    private String mMovieName;
    private String mMovieGenre;
    private String mMovieRanking;
    private String mMoviePosterUrl;
    private String mMovieLaunchDate;
    private String mMovieBackdropUrl;
    private String mMovieOverView;

    /**
     * Construtor da Classe
     */
    public MovieData(int movieId, String movieName, String movieGenre, String movieRanking,
                     String moviePosterUrl, String movieLaunchDate, String movieBackdropUrl, String movieOverView) {
        this.mMovieId = movieId;
        this.mMovieName = movieName;
        this.mMovieGenre = movieGenre;
        this.mMovieRanking = movieRanking;
        this.mMoviePosterUrl = moviePosterUrl;
        this.mMovieLaunchDate = movieLaunchDate;
        this.mMovieBackdropUrl = movieBackdropUrl;
        this.mMovieOverView = movieOverView;
    }

    /**
     * Construtor da Classe utilizando Parcel
     */
    @Ignore
    private MovieData(Parcel in) {
        mMovieId = in.readInt();
        mMovieName = in.readString();
        this.mMovieGenre = in.readString();
        mMovieRanking = in.readString();
        mMoviePosterUrl = in.readString();
        mMovieLaunchDate = in.readString();
        mMovieBackdropUrl = in.readString();
        mMovieOverView = in.readString();
    }

    /**
     * Construtor da Classe vazio para Parceler
     */
    @Ignore
    public MovieData() {
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        this.mMovieId = movieId;
    }

    public String getMovieName() {
        return mMovieName;
    }

    public void setMovieName(String movieName) {
        this.mMovieName = movieName;
    }

    public String getMovieGenre() {
        return mMovieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.mMovieGenre = movieGenre;
    }

    public String getMovieRanking() {
        return mMovieRanking;
    }

    public void setMovieRanking(String movieRanking) {
        this.mMovieRanking = movieRanking;
    }

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public void setmMoviePosterUrl(String moviePosterUrl) {
        this.mMoviePosterUrl = moviePosterUrl;
    }

    public String getMovieLaunchDate() {
        return mMovieLaunchDate;
    }

    public void setmMovieLaunchDate(String movieLaunchDate) {
        this.mMovieLaunchDate = movieLaunchDate;
    }

    public String getMovieBackdropUrl() {
        return mMovieBackdropUrl;
    }

    public void setmMovieBackdropUrl(String movieBackdropUrl) {
        this.mMovieBackdropUrl = movieBackdropUrl;
    }

    public String getMovieOverView() {
        return mMovieOverView;
    }

    public void setmMovieOverView(String movieOverView) {
        this.mMovieOverView = movieOverView;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mMovieId);
        parcel.writeString(mMovieName);
        parcel.writeString(mMovieGenre);
        parcel.writeString(mMovieRanking);
        parcel.writeString(mMoviePosterUrl);
        parcel.writeString(mMovieLaunchDate);
        parcel.writeString(mMovieBackdropUrl);
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
                "MovieId='" + mMovieId + '\'' +
                "MovieName='" + mMovieName + '\'' +
                ", MovieGenre='" + mMovieGenre + '\'' +
                ", MovieRanking='" + mMovieRanking + '\'' +
                ", MoviePosterURL='" + mMoviePosterUrl + '\'' +
                ", MovieLaunchDate='" + mMovieLaunchDate + '\'' +
                ", MovieBackgroundURL='" + mMovieBackdropUrl + '\'' +
                ", MovieOverView='" + mMovieOverView + '\'' +
                '}';
    }
}
