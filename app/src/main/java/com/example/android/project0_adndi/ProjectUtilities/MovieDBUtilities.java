package com.example.android.project0_adndi.ProjectUtilities;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.project0_adndi.MovieData;
import com.example.android.project0_adndi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieDBUtilities {

    public static final String TAG = "MovieDBUtilities";

    public static String getFinalImageURL(String imageUrl, int poster_Backdrop) {

        String finalImageUrl = "";

        final int POSTER_INT = 301;

        final int BACKDROP_INT = 302;

        final String BASE_URL = "http://image.tmdb.org/t/p/";

        final String POSTER_SIZE = "w300";

        final String BACKDROP_SIZE = "w1280";

        if (poster_Backdrop == POSTER_INT) {
            finalImageUrl = BASE_URL + POSTER_SIZE + imageUrl;
        } else if (poster_Backdrop == BACKDROP_INT) {
            finalImageUrl = BASE_URL + BACKDROP_SIZE + imageUrl;
        }

        return finalImageUrl;
    }

    public static int getScoreColor(double movieRankings) {
        Log.v(TAG, "MovieRanking: " + String.valueOf(movieRankings));

        if (movieRankings >= 9)
            return R.color.rankingBest;
        else if (movieRankings >= 8)
            return R.color.rankingGood;
        else if (movieRankings >= 6)
            return R.color.rankingAverage;
        else if (movieRankings >= 4)
            return R.color.rankingBelowAverage;
        else if (movieRankings >= 2)
            return R.color.rankingBad;

        return R.color.rankingTerrible;
    }

    @Nullable
    public static List<MovieData> getMovieDataFromJson(String JsonString) {

        if (TextUtils.isEmpty(JsonString)) {
            return null;
        }

        List<MovieData> movieList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(JsonString);

            JSONArray resultsJson = root.getJSONArray("results");

            for (int resultsIndex = 0; resultsIndex < resultsJson.length(); resultsIndex++) {

                JSONObject thisMovie = resultsJson.getJSONObject(resultsIndex);

                int movieId = thisMovie.optInt("id");
                String movieName = thisMovie.optString("original_title");
                String movieRanking = String.valueOf(thisMovie.optInt("vote_average"));
                String moviePosterUrl = thisMovie.optString("poster_path");
                String movieBackdropURL = thisMovie.optString("backdrop_path");
                String movieLaunchDate = thisMovie.optString("release_date");
                String movieOverView = thisMovie.optString("overview");

                MovieData newMovie = new MovieData(movieId, movieName, movieRanking, moviePosterUrl, movieLaunchDate, movieBackdropURL, movieOverView);

                movieList.add(newMovie);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the news JSON results", e);
        }

        Log.v( TAG, "getMovieDataFromJson: " + movieList );
        return movieList;
    }

    public static int getPagesAndTotalFromJson(String JsonString, String pagesOrTotal) {

        int pages = 1;

        if (TextUtils.isEmpty(JsonString)) {
            return pages;
        }

        try {

            JSONObject root = new JSONObject(JsonString);

            pages = root.getInt(pagesOrTotal);

        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the news JSON results", e);
        }

        return pages;
    }

}
