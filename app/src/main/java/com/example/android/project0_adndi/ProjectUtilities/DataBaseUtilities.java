package com.example.android.project0_adndi.ProjectUtilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUtilities {

    //  TAG desta Classe - para os erros
    private static final String LOG_TAG = DataBaseUtilities.class.getSimpleName();

    // Iniciação da DataBase
    private static AppDatabase mDb;

    /**
     * createMovieListFromUrl cria um objeto UrlMovieList e o salva na DB
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     * @param requestUrl url original da busca
     * @param context    o contexto atual para ser passado para mDb
     */
    public static void createMovieListFromUrl(String JsonString, final String requestUrl, Context context) {

        JSONArray movieList = new JSONArray();

        mDb = AppDatabase.getInstance(context);

        if (TextUtils.isEmpty(JsonString)) {
            return;
        }

        try {

            JSONObject root = new JSONObject(JsonString);

            int totalPages = root.getInt("total_pages");

            final int page = root.getInt("page");

            JSONArray resultsJson = root.getJSONArray("results");

            for (int resultsIndex = 0; resultsIndex < resultsJson.length(); resultsIndex++) {

                JSONObject thisMovie = resultsJson.getJSONObject(resultsIndex);

                int movieId = thisMovie.optInt("id");

                movieList.put(movieId);
            }

            final String movieListString = movieList.toString();

            final UrlMovieList urlList = new UrlMovieList(requestUrl, page, totalPages, movieListString);

            Log.d(LOG_TAG, "createMovieListFromUrl: " + urlList.toString());

            if (mDb.UrlDao().checkIfRequestExists(requestUrl, page)) {
                mDb.UrlDao().updateUrlInfo(urlList);
            } else {
                mDb.UrlDao().addUrlInfo(urlList);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the moviesDB JSON results", e);
        }

    }

    /**
     * getMovieDataFromDB cria uma lista de MovieData a partir de um objeto UrlMovieList
     * do JSON
     *
     * @param urlString url da lista a ser buscada
     * @param page      pagind atual da busca
     * @param context   o contexto atual para ser passado para mDb
     */
    public static List<MovieData> getMovieDataFromDB(final String urlString, final int page, final Context context) {

        mDb = AppDatabase.getInstance(context);

        List<MovieData> movieList = new ArrayList<>();

        if (urlString != null) {
            UrlMovieList movieListUrl = mDb.UrlDao().loadMovieUrlList(urlString, page);
            if (movieListUrl != null) {
                String JsonListString = movieListUrl.getMoviesList();
                movieList = getFilmListArrayFromDbJson(JsonListString, context);
            }
        }
        return movieList;
    }

    /**
     * getFilmListArrayFromDbJson cria uma lista de MovieData a partir do objeto Json salvo no UrlMovieList
     * do JSON
     *
     * @param dbMovieListJson JSOn com os dados brutos dos filmes
     * @param context         o contexto atual para ser passado para mDb
     */
    private static List<MovieData> getFilmListArrayFromDbJson(String dbMovieListJson, Context context) {

        mDb = AppDatabase.getInstance(context);

        final List<MovieData> movieList = new ArrayList<>();

        if (TextUtils.isEmpty(dbMovieListJson)) {
            return null;
        }

        try {
            JSONArray movieIdList = new JSONArray(dbMovieListJson);
            for (int JsonArrayIndex = 0; JsonArrayIndex < movieIdList.length(); JsonArrayIndex++) {

                final int movieId = movieIdList.optInt(JsonArrayIndex);

                MovieData movieData = mDb.MovieDao().loadMovieById(movieId);
                movieList.add(movieData);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the  JSON DB List", e);
        }

        return movieList;
    }

    /**
     * saveMoviesDataToDB salva todos os filmes da lista de MovieData no DB
     * do JSON
     *
     * @param movieList lista de filmes a serem salvas
     * @param context   o contexto atual para ser passado para mDb
     */
    public static void saveMoviesDataToDB(List<MovieData> movieList, Context context) {

        mDb = AppDatabase.getInstance(context);

        if (movieList != null) {
            for (int listIndex = 0; listIndex < movieList.size(); listIndex++) {

                final MovieData movieToAdd = movieList.get(listIndex);

                mDb.MovieDao().addMovie(movieToAdd);
            }
        }
    }

    /**
     * checkIfUrlExists veirifica se existe essa row no DB
     * do JSON
     *
     * @param urlString url original da busca
     * @param page      pagina a ser verificada se existe
     * @param context   o contexto atual para ser passado para mDb
     */
    public static Boolean checkIfUrlExists(final String urlString, final int page, final Context context) {
        mDb = AppDatabase.getInstance(context);
        return mDb.UrlDao().checkIfRequestExists(urlString, page);
    }

}
