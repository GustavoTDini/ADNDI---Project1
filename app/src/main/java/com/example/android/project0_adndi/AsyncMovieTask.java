package com.example.android.project0_adndi;

import android.os.AsyncTask;

import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import java.net.URL;

public class AsyncMovieTask extends AsyncTask <String, Void, String> {

    private AsyncTaskDelegate delegate;

    AsyncMovieTask(AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected String doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }
        // resgata os varios params da solicitação
        String typeAPI = params[0];
        String searchQuery = params[1];
        String APIPages = params[2];

        URL movieUrl = NetworkUtilities.buildMovieSearchURL( typeAPI, searchQuery, APIPages );

        try {
            return NetworkUtilities.getResponseFromHttpUrl( movieUrl );
//            return MovieDBUtilities.getMovieDataFromJson(movieJson);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute( jsonResponse );
        if (delegate != null) delegate.processFinish( jsonResponse );
    }
}