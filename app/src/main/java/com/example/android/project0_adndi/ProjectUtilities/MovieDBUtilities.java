package com.example.android.project0_adndi.ProjectUtilities;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
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

        final String POSTER_SIZE = "w185";

        final String BACKDROP_SIZE = "w300";

        if (poster_Backdrop == POSTER_INT) {
            finalImageUrl = BASE_URL + POSTER_SIZE + imageUrl;
        } else if (poster_Backdrop == BACKDROP_INT) {
            finalImageUrl = BASE_URL + BACKDROP_SIZE + imageUrl;
        }

        return finalImageUrl;
    }

    public static int getScoreColor(double movieRankings) {
        Resources resources = Resources.getSystem();
        int rankColor = ResourcesCompat.getColor(resources, R.color.rankingTerrible, null);

        if (movieRankings > 9)
            rankColor = ResourcesCompat.getColor(resources, R.color.rankingBest, null);
        if (movieRankings > 8)
            rankColor = ResourcesCompat.getColor(resources, R.color.rankingGood, null);
        if (movieRankings > 6)
            rankColor = ResourcesCompat.getColor(resources, R.color.rankingAverage, null);
        if (movieRankings > 4)
            rankColor = ResourcesCompat.getColor(resources, R.color.rankingBelowAverage, null);
        if (movieRankings > 2)
            rankColor = ResourcesCompat.getColor(resources, R.color.rankingBad, null);

        return rankColor;
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


        return movieList;
    }

}
