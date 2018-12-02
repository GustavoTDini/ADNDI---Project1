package com.example.android.project0_adndi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieGridAdapterOnClickHandler {

    private static final String TAG = "MainActivity";

    private static final String SEARCH = "1000";
    private static final String POPULAR = "1010";
    private static final String TOP_RATED = "1100";
    private static final String UPCOMING = "1101";
    private static final String NOW_PLAYING = "1110";


    private static final String MOVIE_PARCEL = "movieParcel";
    private static final int LANDSCAPE_SPAM = 3;
    private static final int PORTRAIT_SPAM = 2;
    int JsonPAges = 1;
    String query = "";

    private MovieGridAdapter mMovieGridAdapter;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_list);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mProgressBar = findViewById(R.id.pb_loading_indicator);


        int orientation = getResources().getConfiguration().orientation;

        int gridSpam = PORTRAIT_SPAM;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridSpam = LANDSCAPE_SPAM;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSpam);

        mRecyclerView.setLayoutManager(layoutManager);

        mMovieGridAdapter = new MovieGridAdapter(this);

        mRecyclerView.setAdapter(mMovieGridAdapter);

        testConnectionAndStartAsync(POPULAR);

    }

    @Override
    public void onClick(MovieData movie) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent detailsIntent = new Intent(context, destinationClass);
        detailsIntent.putExtra(MOVIE_PARCEL, movie);
        startActivity(detailsIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popular:
                testConnectionAndStartAsync(POPULAR);
                return true;
            case R.id.action_top_rated:
                testConnectionAndStartAsync(TOP_RATED);
                return true;
            case R.id.action_upcoming:
                testConnectionAndStartAsync(UPCOMING);
                return true;
            case R.id.action_now_playing:
                testConnectionAndStartAsync(NOW_PLAYING);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showViews(Boolean error, String errorText) {
        if (error) {
            Log.v(TAG, "ShowViews " + error.toString());
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(errorText);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void testConnectionAndStartAsync(String type) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Log.v(TAG, "NetworkInfo " + networkInfo);
        if (networkInfo != null && networkInfo.isConnected()) {
            String stringPages = String.valueOf(JsonPAges);
            if (type.equals(SEARCH)) {
                new AsyncMovieTask().execute(type, query, stringPages);
            } else {
                new AsyncMovieTask().execute(type, null, stringPages);
            }

        } else {
            showViews(true, "There´s no Internet Connection!!");
        }
    }

    public class AsyncMovieTask extends AsyncTask<String, Void, List<MovieData>> {

        private final String TAG = "AsyncMovieTask";

        private int asyncPages;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<MovieData> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String typeAPI = params[0];
            String searchQuery = params[1];
            String APIPages = params[2];

            URL movieUrl = NetworkUtilities.buildMovieSearchURL(typeAPI, searchQuery, APIPages);
            Log.v(TAG, movieUrl.toString());

            try {
                String movieJson = NetworkUtilities.getResponseFromHttpUrl(movieUrl);
                asyncPages = MovieDBUtilities.getPagesFromJson(movieJson);
                return MovieDBUtilities.getMovieDataFromJson(movieJson);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<MovieData> movieList) {
            if (movieList == null || movieList.size() > 0) {
                mMovieGridAdapter.setMovieData(movieList);
                JsonPAges = asyncPages;
                mProgressBar.setVisibility(View.INVISIBLE);
                showViews(false, null);
                Log.d("AsyncMovie", "onPostExecute: " + movieList);
            } else {
                showViews(true, "An error has occurred. Please try again");
            }
        }
    }


}
