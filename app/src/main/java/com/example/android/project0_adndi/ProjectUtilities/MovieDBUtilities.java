package com.example.android.project0_adndi.ProjectUtilities;

public final class MovieDBUtilities {

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




}
