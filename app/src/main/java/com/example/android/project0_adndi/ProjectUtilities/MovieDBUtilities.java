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

    // Código do Poster
    public static final int POSTER_INT = 301;
    // Código do BackDrop
    public static final int BACKDROP_INT = 302;
    //  TAG desta Classe - para os erros
    private static final String LOG_TAG = MovieDBUtilities.class.getSimpleName();


    /**
     * getFinalImageURL é utilizado para criar o URL da imagem do poster ou fundo
     *
     * @param imageUrl        parte da URL recebido pela MOVIEDB JSON
     * @param poster_Backdrop int que define se criremos uma URL do poster ou do Fundo
     * @return a String com a URL da Imagem
     */
    public static String getFinalImageURL(String imageUrl, int poster_Backdrop) {

        String finalImageUrl = "";

        // URL base da image
        final String BASE_URL = "http://image.tmdb.org/t/p/";

        // tamanho do poster
        final String POSTER_SIZE = "w300";

        // tamanho do BackDrop
        final String BACKDROP_SIZE = "w1280";

        if (poster_Backdrop == POSTER_INT) {
            finalImageUrl = BASE_URL + POSTER_SIZE + imageUrl;
        } else if (poster_Backdrop == BACKDROP_INT) {
            finalImageUrl = BASE_URL + BACKDROP_SIZE + imageUrl;
        }

        return finalImageUrl;
    }

    /**
     * getScoreColor define a cor da View da pontuação do Filme
     *
     * @param movieRankings Nota do Filme
     * @return int com o valor da cor
     */
    public static int getScoreColor(double movieRankings) {

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

    /**
     * getMovieDataFromJson decodifica o filme do JSON em uma lista de MovieData, de modo
     * que podemos recuperar esses dados em outras partes do App
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     *
     * @return List de MovieData com os filmes que estavam na JSON
     */
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
            Log.e( LOG_TAG, "Problem parsing the news JSON results", e );
        }

        return movieList;
    }

    /**
     * getPagesAndTotalFromJson retorna ou o total de paginas ou a pagina atual da lista de filmes
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     *
     * @param pagesOrTotal string que pode ser 'page' ou 'total_pages', para retornarmos uma ou outra informação
     *
     * @return int com o numero de paginas ou pagina atual a depender de pagesOrTotal
     */
    public static int getPagesAndTotalFromJson(String JsonString, String pagesOrTotal) {

        int pages = 1;

        if (TextUtils.isEmpty(JsonString)) {
            return pages;
        }

        try {
            JSONObject root = new JSONObject(JsonString);
            pages = root.getInt(pagesOrTotal);
        } catch (JSONException e) {
            Log.e( LOG_TAG, "Problem parsing the news JSON results", e );
        }

        return pages;
    }

}
