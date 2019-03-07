package com.example.android.project0_adndi;

import android.os.AsyncTask;

import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import java.net.URL;

public class AsyncMovieTask extends AsyncTask<URL, Void, String> {

    private AsyncTaskDelegate delegate;


    AsyncMovieTask(AsyncTaskDelegate responder) {
        this.delegate = responder;
    }

    @Override
    protected String doInBackground(URL... params) {

        if (params.length == 0) {
            return null;
        }
        // resgata o Url de solicitação
        URL searchUrl = params[0];

        try {
            return NetworkUtilities.getResponseFromHttpUrl(searchUrl);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String jsonResponse) {
        super.onPostExecute( jsonResponse );
        if (delegate != null) delegate.processFinish(jsonResponse);
    }
}