package com.example.android.project0_adndi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieGridAdapterOnClickHandler {

    private static final String SEARCH = "1000";
    private static final String POPULAR = "1010";
    private static final String TOP_RATED = "1100";
    private static final String UPCOMING = "1101";
    private static final String NOW_PLAYING = "1110";

    private RecyclerView mRecyclerView;
    private MovieGridAdapter mMovieGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById( R.id.rv_movie_list );

        GridLayoutManager layoutManager = new GridLayoutManager( this, 2 );

        mRecyclerView.setLayoutManager( layoutManager );

        mMovieGridAdapter = new MovieGridAdapter( this );

        mRecyclerView.setAdapter( mMovieGridAdapter );

        new AsyncMovieTask().execute( POPULAR );
    }

    @Override
    public void onClick(int movieId) {

    }
}
