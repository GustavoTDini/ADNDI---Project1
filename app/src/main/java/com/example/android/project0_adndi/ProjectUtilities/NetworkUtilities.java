package com.example.android.project0_adndi.ProjectUtilities;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class NetworkUtilities {

    //  TAG desta Classe - para os erros
    private static final String TAG = NetworkUtilities.class.getSimpleName();

    // Strings com cada codigo dos diferentes tipos de queries
    private static final String SEARCH = "1000";
    private static final String POPULAR = "1010";
    private static final String TOP_RATED = "1100";
    private static final String UPCOMING = "1101";
    private static final String NOW_PLAYING = "1110";

    // Codigo de resposta ok da conexão
    private static final int URL_CONNECTION_GET_RESPONSE_CODE = 200;

    // Base da URL do MovieDB
    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/";

    // Endereços de cada query do MovieDB URL
    private static final String SEARCH_URL = "search/movie";
    private static final String POPULAR_URL = "movie/popular";
    private static final String TOP_RATED_URL = "movie/top_rated";
    private static final String UPCOMING_URL = "movie/upcoming";
    private static final String NOW_PLAYING_URL = "movie/now_playing";

    // Chave da API
    private static final String MOVIE_DB_API_KEY = "API KEY";

    // COdigo da Lingua definida
    private static final String LANGUAGE = "en";

    // String para não incluir conteudos adultos
    private static final String INCLUDE_ADULT = "false";

    // os Varios parametros da Query do MovieDB
    private final static String QUERY_PARAM = "query";
    private final static String PAGE_PARAM = "page";
    private final static String API_PARAM = "api_key";
    private final static String LANGUAGE_PARAM = "language";
    private final static String ADULT_PARAM = "include_adult";

    /**
     * buildMovieSearchURL é chamado para criarmos a URL do MovieDB, de acordo com as opções do Usuario
     *
     * @param type  o tipo de busca que iremos iniciar com a MOVIEDB
     * @param query se o query for tipo search, este é o string a ser buscado
     * @param page  a pagina em que se encontra a query
     * @return a URL com os valores selecionados
     */
    public static URL buildMovieSearchURL(String type, String query, String page) {
        Uri APIUri;
        URL APIUrl = null;

        // utilizado um switch para os varios métodos de busca
        switch (type) {
            case SEARCH:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + SEARCH_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .appendQueryParameter(QUERY_PARAM, query)
                        .appendQueryParameter(ADULT_PARAM, INCLUDE_ADULT)
                        .build();
                break;
            case POPULAR:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + POPULAR_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case TOP_RATED:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + TOP_RATED_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case UPCOMING:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + UPCOMING_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            case NOW_PLAYING:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + NOW_PLAYING_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
                break;
            default:
                APIUri = Uri.parse(MOVIE_DB_BASE_URL + POPULAR_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(API_PARAM, MOVIE_DB_API_KEY)
                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                        .build();
        }

        // Verifica se o URL é valido
        try {
            APIUrl = new URL(APIUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return APIUrl;
    }

    /**
     * readFromStream é utilizado para lermos o fluxo de informações da Conexão e criarmos o JSON
     *
     * @param inputStream fluxo de informação da conexão aberta
     * @return a String com o JSON retornado
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * getResponseFromHttpUrl é utilizado para abrirmos uma conexão com o respectivo URL e retornar
     * o JSON com as informações
     *
     * @param movieUrl URL criado pelas opções do usuario
     *
     * @return a String com o JSON retornado
     */
    public static String getResponseFromHttpUrl(URL movieUrl) throws IOException {
        String jsonResponse = "";

        if (movieUrl == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) movieUrl.openConnection();
            urlConnection.connect();
            if (urlConnection.getResponseCode() == URL_CONNECTION_GET_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(TAG, " Problem in makeHttpRequest" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

}
