package com.example.android.project0_adndi;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import java.net.URL;
import java.util.List;

public class AsyncMovieTask extends AsyncTask <String, Void, List <MovieData>> {

    final String TAG = "AsyncMovieTask";
    MovieGridAdapter mMovieGridAdapter;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List <MovieData> doInBackground(String... params) {


        if (params.length == 0) {
            return null;
        }

        String typeAPI = params[0];
        URL movieUrl = NetworkUtilities.buildMovieSearchURL( typeAPI, null );
        Log.v( TAG, movieUrl.toString() );

        try {
            String movieJson = NetworkUtilities.getResponseFromHttpUrl( movieUrl );

            return MovieDBUtilities.getMovieDataFromJson( movieJson );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List <MovieData> movieList) {
        if (movieList != null) {
            mMovieGridAdapter.setMovieData( movieList );
            Log.d( "AsyncMovie", "onPostExecute: " + movieList );
        }
    }
}