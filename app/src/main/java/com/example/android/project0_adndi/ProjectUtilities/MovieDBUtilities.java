package com.example.android.project0_adndi.ProjectUtilities;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.MovieReviews;
import com.example.android.project0_adndi.DataUtilities.MovieVideos;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;
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
    public static final String YOUTUBE_PATH = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_IMG = "/0.jpg";

    //  TAG desta Classe - para os erros
    private static final String LOG_TAG = MovieDBUtilities.class.getSimpleName();
    private static final SparseArray<String> gendersMap = new SparseArray<String>() {
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

//    /**
//     * getFinalImageUrl é utilizado para criar o URL da imagem do poster ou fundo
//     *
//     * @param imageUrlString parte da URL recebido pela MOVIEDB JSON
//     * @return a String com a URL da Imagem
//     */
//    private static void saveImageToInternal(String imageUrlString, Context context, int poster_Backdrop) {
//
//        ContextWrapper cw = new ContextWrapper(context);
//
//        String directoryName = "movieImageDir";
//
//        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
//
//        // Create imageDir
//        File myPath = new File(directory, imageUrlString);
//
//        try {
//            Uri imageUri = Uri.parse(getFinalImageUrl(imageUrlString, poster_Backdrop));
//            try {
//                Bitmap movieImage = Downloader.Response
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Downloader.Response
//            DownloadManager.Request request = new DownloadManager.Request(imageUri).setDestinationInExternalFilesDir(context, directoryName, imageUrlString);
//            DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//            downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
//
//            Log.d(LOG_TAG, "saveImageToInternal: " + fos);
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * getScoreColor define a cor da View da pontuação do Filme
     *
     * @param movieRankings Nota do Filme
     * @return int com o valor da cor
     */
    public static int getScoreColor(int movieRankings) {

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
    private static String getMovieGenre(JSONArray genderArray) {

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

        Log.d(LOG_TAG, "getMovieGenre: " + String.valueOf(genderNames));

        return String.valueOf(genderNames);

    }

    /**
     * getMovieDataFromJson decodifica o filme do JSON em uma lista de MovieData, de modo
     * que podemos recuperar esses dados em outras partes do App
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
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

                Log.d(LOG_TAG, "getMovieDataFromJson: " + thisMovie);

                final int movieId = thisMovie.optInt("id");
                String movieName = thisMovie.optString("original_title");
                String movieGenre = getMovieGenre(thisMovie.optJSONArray("genre_ids"));
                String movieRanking = String.valueOf(thisMovie.optInt("vote_average"));
                String moviePosterUrl = thisMovie.optString("poster_path");
                String movieBackdropURL = thisMovie.optString("backdrop_path");
                String movieLaunchDate = thisMovie.optString("release_date");
                String movieOverView = thisMovie.optString("overview");

                final MovieData newMovie = new MovieData(movieId, movieName, movieGenre, movieRanking, moviePosterUrl, movieLaunchDate, movieBackdropURL, movieOverView);

                movieList.add(newMovie);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return movieList;

    }

    /**
     * extractTotalPages retorna o total de paginas da lista de filmes
     * do JSON
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
    public static int extractTotalPages(String JsonString) {

        int page = 1;
        if (TextUtils.isEmpty(JsonString)) {
            return page;
        }

        try {
            JSONObject root = new JSONObject(JsonString);
            page = root.getInt("total_pages");

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return page;
    }

    /**
     * extractPAge retorna a pagina atual da lista de filmes
     * do JSON
     *
     * @param JsonArray JSOn com os dados brutos dos filmes
     */
    public static List<MovieData> getFilmListArrayFromDb(String JsonArray, Context context) {

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
                        MovieData movieData = mDb.MovieDao().loadMovieById(movieId);
                        movieList.add(movieData);
                    }
                });

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return movieList;
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

            final UrlMovieList urlList = new UrlMovieList(requestUrl, page, totalPages, movieListString);

            Log.d(LOG_TAG, "createMovieListFromUrl: " + urlList.toString());

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb.MovieDao().checkIfRequestExists(requestUrl, page)) {
                        mDb.MovieDao().updateUrlInfo(urlList);
                    } else {
                        mDb.MovieDao().addUrlInfo(urlList);
                    }
                }
            });

        } catch (JSONException e) {
            Log.e( LOG_TAG, "Problem parsing the news JSON results", e );
        }

    }

    /**
     * createMovieListFromUrl retorna ou o total de paginas ou a pagina atual da lista de filmes
     * do JSON
     *
     * @param movieList JSOn com os dados brutos dos filmes
     *
     * @param context url original da busca
     */
    public static void saveMoviesDataToDB(List<MovieData> movieList, Context context) {

        mDb = AppDatabase.getInstance(context);

        for (int listIndex = 0; listIndex < movieList.size(); listIndex++) {

            final MovieData movieToAdd = movieList.get(listIndex);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb.MovieDao().checkIfMovieExists(movieToAdd.getMovieId())) {
                        mDb.MovieDao().updateMovie(movieToAdd);
                    } else {
                        mDb.MovieDao().addMovie(movieToAdd);
                    }
                }
            });
//
//            saveImageToInternal(movieToAdd.getMovieBackdropUrl(), context);
//            saveImageToInternal(movieToAdd.getMoviePosterUrl(), context);
        }

    }

    /**
     * getMovieDataFromJson decodifica o filme do JSON em uma lista de MovieData, de modo
     * que podemos recuperar esses dados em outras partes do App
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
    public static List<MovieReviews> getMovieReviewsFromJson(String JsonString) {

        if (TextUtils.isEmpty(JsonString)) {
            return null;
        }

        List<MovieReviews> reviewsList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(JsonString);

            final int movieId = root.optInt("id");

            JSONArray resultsJson = root.getJSONArray("results");

            for (int resultsIndex = 0; resultsIndex < resultsJson.length(); resultsIndex++) {

                JSONObject thisMovie = resultsJson.getJSONObject(resultsIndex);

                String reviewAuthor = thisMovie.optString("author");
                String reviewContent = thisMovie.optString("content");


                final MovieReviews review = new MovieReviews(movieId, reviewAuthor, reviewContent);

                reviewsList.add(review);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        return reviewsList;

    }

    /**
     * createMovieListFromUrl retorna ou o total de paginas ou a pagina atual da lista de filmes
     * do JSON
     *
     * @param reviewsList JSOn com os dados brutos dos filmes
     * @param context     url original da busca
     */
    public static void saveReviewsDataToDB(List<MovieReviews> reviewsList, Context context) {

        mDb = AppDatabase.getInstance(context);

        for (int listIndex = 0; listIndex < reviewsList.size(); listIndex++) {

            final MovieReviews reviewsToAdd = reviewsList.get(listIndex);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (mDb.MovieDao().checkIfReviewsExists(reviewsToAdd.getMovieId(), reviewsToAdd.getReviewAuthor())) {
                        mDb.MovieDao().updateReviewsInfo(reviewsToAdd);
                    } else {
                        mDb.MovieDao().addReviewsData(reviewsToAdd);
                    }
                }
            });
//
//            saveImageToInternal(movieToAdd.getMovieBackdropUrl(), context);
//            saveImageToInternal(movieToAdd.getMoviePosterUrl(), context);
        }

    }

    /**
     * getMovieDataFromJson decodifica o filme do JSON em uma lista de MovieData, de modo
     * que podemos recuperar esses dados em outras partes do App
     *
     * @param JsonString JSOn com os dados brutos dos filmes
     */
    public static List<MovieVideos> getMovieVideosFromJson(String JsonString) {

        if (TextUtils.isEmpty(JsonString)) {
            return null;
        }

        List<MovieVideos> videosList = new ArrayList<>();

        try {

            JSONObject root = new JSONObject(JsonString);

            final int movieId = root.optInt("id");

            JSONArray resultsJson = root.getJSONArray("results");

            for (int resultsIndex = 0; resultsIndex < resultsJson.length(); resultsIndex++) {

                JSONObject videoData = resultsJson.getJSONObject(resultsIndex);

                if (videoData.optString("site").equals("YouTube")) {
                    String videoTitle = videoData.optString("name");
                    String videoKey = videoData.optString("key");


                    final MovieVideos video = new MovieVideos(movieId, videoTitle, videoKey);

                    videosList.add(video);
                }

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        return videosList;

    }

    public static String getYoutubeThumbnailPath(String videoKey) {
        return YOUTUBE_PATH + videoKey + YOUTUBE_IMG;
    }

}
