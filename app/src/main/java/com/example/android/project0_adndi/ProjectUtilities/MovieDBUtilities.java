package com.example.android.project0_adndi.ProjectUtilities;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.example.android.project0_adndi.AppExecutors;
import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;
import com.example.android.project0_adndi.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MovieDBUtilities {

    // Código do Poster
    public static final int POSTER_INT = 301;
    // Código do BackDrop
    public static final int BACKDROP_INT = 302;
    //  TAG desta Classe - para os erros
    private static final String LOG_TAG = MovieDBUtilities.class.getSimpleName();
    static final SparseArray<String> gendersMap = new SparseArray<String>() {
        {
            append(28, "Action");
            append(12, "Adventure");
            append(16, "Animation");
            append(35, "Comedy");
            append(80, "Crime");
            append(99, "Documentary");
            append(18, "Drama");
            append(10751, "Family");
            append(14, "Fantasy");
            append(36, "History");
            append(27, "Horror");
            append(10402, "Music");
            append(9648, "Mystery");
            append(10749, "Romance");
            append(878, "Science Fiction");
            append(10770, "TV Movie");
            append(53, "Thriller");
            append(10752, "War");
            append(37, "Western");

        }
    };
    // Iniciação da DataBase
    private static AppDatabase mDb;

    /**
     * getFinalImageUrl é utilizado para criar o URL da imagem do poster ou fundo
     *
     * @param imageUrl        parte da URL recebido pela MOVIEDB JSON
     * @param poster_Backdrop int que define se criremos uma URL do poster ou do Fundo
     * @return a String com a URL da Imagem
     */
    public static String getFinalImageUrl(String imageUrl, int poster_Backdrop) {

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
     * getFinalImageUrl é utilizado para criar o URL da imagem do poster ou fundo
     *
     * @param imageUrl        parte da URL recebido pela MOVIEDB JSON
     * @param poster_Backdrop int que define se criremos uma URL do poster ou do Fundo
     * @return a String com a URL da Imagem
     */
    public static String saveImagetoInternal(String imageUrl, Context context, int poster_Backdrop) {

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir

        String directoryName = "movieImageDirBackdrop";

        if (poster_Backdrop == POSTER_INT) {
            directoryName = "movieImageDirPoster";
        }

        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);

        // Create imageDir
        File mypath = new File(directory, ".jpg");


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            Bitmap movieImage = Picasso.with(context).load(imageUrl).get();
            // Use the compress method on the BitMap object to write image to the OutputStream
            movieImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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
     * getMovieGenre aceita o genderArry recebido do Json com os generos separados e retorna uma String com todos
     * os generos separados por virgula.
     *
     * @param genderArray Array de int com os codigos dos generos do filme
     * @return String com os generos separados por ", "
     */
    public static String getMovieGenre(JSONArray genderArray) {

        ArrayList<String> movieGenders = new ArrayList<>();
        StringBuilder genderNames = new StringBuilder("Not Classified");

        if (genderArray == null) {
            return genderNames.toString();
        }

        for (int genderIndex = 0; genderIndex < genderArray.length(); genderIndex++) {

            String movieGenderName = gendersMap.get(genderArray.optInt(genderIndex));

            movieGenders.add(movieGenderName);
        }

        genderNames = new StringBuilder();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            genderNames = new StringBuilder(String.join(", ", movieGenders));
        } else {
            for (String name : movieGenders) {
                genderNames.append(name).append(", ");
            }

        }

        return String.valueOf(genderNames);

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
    public static void getMovieDataFromJson(String JsonString, Context context) {

        mDb = AppDatabase.getInstance(context);

        if (TextUtils.isEmpty(JsonString)) {
            return;
        }

        try {

            JSONObject root = new JSONObject(JsonString);

            JSONArray resultsJson = root.getJSONArray("results");

            for (int resultsIndex = 0; resultsIndex < resultsJson.length(); resultsIndex++) {

                JSONObject thisMovie = resultsJson.getJSONObject(resultsIndex);

                final int movieId = thisMovie.optInt("id");
                String movieName = thisMovie.optString("original_title");
                String movieGenre = getMovieGenre(thisMovie.optJSONArray("genders"));
                String movieRanking = String.valueOf(thisMovie.optInt("vote_average"));
                String moviePosterUrl = saveImagetoInternal(getFinalImageUrl(thisMovie.optString("poster_path"), POSTER_INT), context, POSTER_INT);
                String movieBackdropURL = saveImagetoInternal(getFinalImageUrl(thisMovie.optString("backdrop_path"), BACKDROP_INT), context, BACKDROP_INT);
                String movieLaunchDate = thisMovie.optString("release_date");
                String movieOverView = thisMovie.optString("overview");

                final MovieData newMovie = new MovieData(movieId, movieName, movieGenre, movieRanking, moviePosterUrl, movieLaunchDate, movieBackdropURL, movieOverView);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mDb.MovieDao().checkIfMovieExists(movieId)) {
                            // update Movie
                            mDb.MovieDao().updateMovie(newMovie);
                        } else {
                            //add New Movie
                            mDb.MovieDao().addMovie(newMovie);
                        }
                    }
                });

            }

        } catch (JSONException e) {
            Log.e( LOG_TAG, "Problem parsing the news JSON results", e );
        }


    }

    /**
     * createMovieListFromUrl retorna ou o total de paginas ou a pagina atual da lista de filmes
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     *
     * @param requestUrl url original da busca
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
            Log.d(LOG_TAG, "createMovieListFromUrl: " + movieListString);

            final UrlMovieList urlList = new UrlMovieList(requestUrl, page, totalPages, movieListString);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb.MovieDao().checkIfRequestExists(requestUrl, page) != null) {
                        // update Movie
                        mDb.MovieDao().updateUrlInfo(urlList);
                    } else {
                        //add New Movie
                        mDb.MovieDao().addUrlInfo(urlList);
                    }
                }
            });

        } catch (JSONException e) {
            Log.e( LOG_TAG, "Problem parsing the news JSON results", e );
        }

    }

    /**
     * extractPAge retorna a pagina atual da lista de filmes
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
    public static int extractPage(String JsonString) {

        int page = 1;
        if (TextUtils.isEmpty(JsonString)) {
            return page;
        }

        try {
            JSONObject root = new JSONObject(JsonString);
            page = root.getInt("page");

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return page;
    }

    /**
     * extractPAge retorna a pagina atual da lista de filmes
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
    public static List<MovieData> getFilmListArrayFromDb(String JsonArray, Context context) {

        Log.d(LOG_TAG, "getFilmListArrayFromDb: " + JsonArray);

        mDb = AppDatabase.getInstance(context);

        final List<MovieData> movieList = new ArrayList<>();

        if (TextUtils.isEmpty(JsonArray)) {
            return null;
        }

        try {
            JSONArray movieIdList = new JSONArray(JsonArray);
            for (int JsonArrayIndex = 0; JsonArrayIndex < movieIdList.length(); JsonArrayIndex++) {

                final int movieId = movieIdList.optInt(JsonArrayIndex);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        LiveData<MovieData> movieLiveData = mDb.MovieDao().loadMovieById(movieId);
                        MovieData movieData = movieLiveData.getValue();
                        movieList.add(movieData);
                    }
                });

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return movieList;
    }

}
