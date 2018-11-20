package com.example.android.project0_adndi.ProjectUtilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtilities {

    private static final String TAG = NetworkUtilities.class.getSimpleName();

    private static final int SEARCH = 1000;
    private static final int LATEST = 1001;
    private static final int POPULAR = 1010;
    private static final int TOP_RATED = 1100;
    private static final int UPCOMING = 1101;
    private static final int NOW_PLAYING = 1110;

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/";

    private static final String SEARCH_URL = "search/movie";
    private static final String LATEST_URL = "movie/latest";
    private static final String POPULAR_URL = "movie/popular";
    private static final String TOP_RATED_URL = "movie/top_rated";
    private static final String UPCOMING_URL = "movie/upcoming";
    private static final String NOW_PLAYING_URL = "movie/now_playing";

    private static final String MOVIE_DB_API_KEY = "{API}";

    private static final String LANGUAGE = "pt-BR";
    private static final String INCLUDE_ADULT = "false";

    private final static String QUERY_PARAM = "query";
    private final static String API_PARAM = "api_key";
    private final static String LANGUAGE_PARAM = "language";
    private final static String ADULT_PARAM = "include_adult";


    public static URL buildMovieSearchURL(int type, String query) {
        Uri APIUri;

        URL APIUrl = null;

        switch (type) {
            case SEARCH:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + SEARCH_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .appendQueryParameter(QUERY_PARAM, query)
                        .appendQueryParameter(ADULT_PARAM, INCLUDE_ADULT)
                        .build();
                break;
            case LATEST:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + LATEST_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case POPULAR:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + POPULAR_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case TOP_RATED:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + TOP_RATED_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case UPCOMING:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + UPCOMING_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case NOW_PLAYING:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + NOW_PLAYING_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            default:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + POPULAR_URL).buildUpon()
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
        }


        try {
            APIUrl = new URL(APIUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + APIUrl);

        return APIUrl;

    }

    public static String getResponseFromHttpUrl(URL movieUrl) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) movieUrl.openConnection();
        try {
            InputStream input = new BufferedInputStream(urlConnection.getInputStream());
            return String.valueOf(input.read());
        } finally {
            urlConnection.disconnect();
        }
    }

}
