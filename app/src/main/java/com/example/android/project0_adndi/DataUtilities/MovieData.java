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
    private String mMoviePosterDirPath;
    private String mMovieLaunchDate;
    private String mMovieBackdropDirPath;
    private String mMovieOverView;

    /**
     * Construtor da Classe
     */
    public MovieData(int movieId, String movieName, String movieGenre, String movieRanking,
                     String moviePosterDirPath, String movieLaunchDate, String movieBackdropDirPath, String movieOverView) {
        this.mMovieId = movieId;
        this.mMovieName = movieName;
        this.mMovieGenre = movieGenre;
        this.mMovieRanking = movieRanking;
        this.mMoviePosterDirPath = moviePosterDirPath;
        this.mMovieLaunchDate = movieLaunchDate;
        this.mMovieBackdropDirPath = movieBackdropDirPath;
        this.mMovieOverView = movieOverView;
    }

    /**
     * Construtor da Classe utilizando Parcel
     */
    @Ignore
    private MovieData(Parcel in) {
        mMovieId = in.readInt();
        mMovieName = in.readString();
        mMovieRanking = in.readString();
        mMoviePosterDirPath = in.readString();
        mMovieLaunchDate = in.readString();
        mMovieBackdropDirPath = in.readString();
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

    public String getMoviePosterDirPath() {
        return mMoviePosterDirPath;
    }

    public void setmMoviePosterDirPath(String moviePosterUrl) {
        this.mMoviePosterDirPath = moviePosterUrl;
    }

    public String getMovieLaunchDate() {
        return mMovieLaunchDate;
    }

    public void setmMovieLaunchDate(String movieLaunchDate) {
        this.mMovieLaunchDate = movieLaunchDate;
    }

    public String getMovieBackdropDirPath() {
        return mMovieBackdropDirPath;
    }

    public void setmMovieBackdropDirPath(String movieBackdropUrl) {
        this.mMovieBackdropDirPath = movieBackdropUrl;
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
        parcel.writeString(mMoviePosterDirPath);
        parcel.writeString(mMovieLaunchDate);
        parcel.writeString(mMovieBackdropDirPath);
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
                ", MoviePosterURL='" + mMoviePosterDirPath + '\'' +
                ", MovieLaunchDate='" + mMovieLaunchDate + '\'' +
                ", MovieBackgroundURL='" + mMovieBackdropDirPath + '\'' +
                ", MovieOverView='" + mMovieOverView + '\'' +
                '}';
    }
}
