package com.example.android.project0_adndi;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.FavoriteMovies;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.MovieReviews;
import com.example.android.project0_adndi.DataUtilities.MovieVideos;
import com.example.android.project0_adndi.ProjectUtilities.AppExecutors;
import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements MovieVideosAdapter.MovieVideosAdapterOnClickHandler {

    final String LOG_TAG = DetailsActivity.class.getSimpleName();

    List<MovieReviews> movieReviewsList;

    List<MovieVideos> movieVideosList;

    RecyclerView mMovieDetailsReviewsRecycler;

    RecyclerView mMovieDetailsVideosRecycler;

    int mMovieId = 0;

    // Iniciação da DataBase
    private static AppDatabase mDb;

    private Menu mFavoriteStarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView mMovieDetailTitleTextView = findViewById(R.id.tv_details_movie_title);
        TextView mMovieDetailRankingTextView = findViewById(R.id.tv_details_movie_ranking);
        TextView mMovieDetailOverviewTextView = findViewById(R.id.tv_details_movie_overview);
        TextView mMovieDetailDateTextView = findViewById(R.id.tv_details_movie_date);
        TextView mMovieDetailGenreTextView = findViewById(R.id.tv_details_movie_genres);
        ImageView mMovieDetailPosterImageView = findViewById(R.id.iv_details_movie_poster);
        ImageView mMovieDetailBackdropImageView = findViewById(R.id.iv_details_background);
        mMovieDetailsReviewsRecycler = findViewById(R.id.rv_review_list);
        mMovieDetailsVideosRecycler = findViewById(R.id.rv_videos_list);


        Intent intentThatStartedThisActivity = getIntent();

        //Verifica se o Intent tem o MOVIE_PARCEL extra
        if (intentThatStartedThisActivity.hasExtra( MovieData.MOVIE_PARCEL )) {
            MovieData movie = Parcels.unwrap( getIntent().getParcelableExtra( MovieData.MOVIE_PARCEL ) );
            Log.d(LOG_TAG, "onCreate: " + movie);
            //resgata os dados sobre o filme do MovieData movie
            String movieTitle = movie.getMovieName();
            String movieDate = movie.getMovieLaunchDate();
            String movieGenres = movie.getMovieGenre();
            String movieOverview = movie.getMovieOverView();
            String movieRanking = movie.getMovieRanking();
            String moviePosterUrl = movie.getMoviePosterUrl();
            String movieBackgroundUrl = movie.getMovieBackdropUrl();
            mMovieId = movie.getMovieId();

            // Define as View com os Valores do MovieData movie
            Picasso.with(getBaseContext()).load(MovieDBUtilities.getFinalImageUrl(moviePosterUrl, MovieDBUtilities.POSTER_INT)).into(mMovieDetailPosterImageView);
            Picasso.with(getBaseContext()).load(MovieDBUtilities.getFinalImageUrl(movieBackgroundUrl, MovieDBUtilities.BACKDROP_INT)).into(mMovieDetailBackdropImageView);
            mMovieDetailDateTextView.setText(movieDate);
            mMovieDetailTitleTextView.setText(movieTitle);
            mMovieDetailOverviewTextView.setText(movieOverview);
            mMovieDetailRankingTextView.setText(movieRanking);
            mMovieDetailGenreTextView.setText(movieGenres);
            int colorInt = MovieDBUtilities.getScoreColor(Integer.parseInt(movieRanking));
            int colorId = ContextCompat.getColor(this, colorInt);
            mMovieDetailRankingTextView.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);

        }

        testConnectionGetAndSaveReviewsData();
        testConnectionAndGetVideosData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        MenuItem favoriteStar = menu.findItem(R.id.action_favorite_star);

        if (MovieDBUtilities.checkIfMovieIsFavorite(mMovieId, getApplicationContext())) {
            favoriteStar.setIcon(R.drawable.baseline_star_white_36);
        } else {
            favoriteStar.setIcon(R.drawable.baseline_star_border_white_36);
        }

        mFavoriteStarMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite_star) {
            addRemoveFromFavorites();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método em Override para modificar o funcionamento do Navigate Up, desse modo
     * ao retornarmos a atividade anterior, a busca continuará a mesma
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(MovieVideos video) {
        openYoutubeIntent(video.getVideoKey());
    }

    /**
     * populateGridAdapter Método que recebe a de reviews do filme e a coloca no RecyclerView
     *
     * @param reviewsList lista de reviews do filme a ser colocada no grid
     */
    private void populateReviewsList(List<MovieReviews> reviewsList) {
        if (reviewsList != null) {
            if (reviewsList.size() > 0) {
                // use a linear layout manager
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                mMovieDetailsReviewsRecycler.setLayoutManager(layoutManager);
                MovieReviewsAdapter mMovieReviewsAdapter = new MovieReviewsAdapter(reviewsList);
                mMovieDetailsReviewsRecycler.setAdapter(mMovieReviewsAdapter);
            } else Log.d(LOG_TAG, "populateGridAdapter: Your search returned no Movies");
        } else Log.d(LOG_TAG, "populateGridAdapter: An error has occurred. Please try again");
    }


    private void testConnectionGetAndSaveReviewsData() {
        Boolean connected = NetworkUtilities.testConnection(getApplicationContext());
        if (connected) {
            final URL reviewsUrl = NetworkUtilities.createVideosReviewsUrl(String.valueOf(mMovieId), NetworkUtilities.REVIEWS);
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String reviewsJson = NetworkUtilities.getResponseFromHttpUrl(reviewsUrl);
                        movieReviewsList = MovieDBUtilities.getMovieReviewsFromJson(reviewsJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (movieReviewsList != null) {
                                populateReviewsList(movieReviewsList);
                                MovieDBUtilities.saveReviewsDataToDB(movieReviewsList, getApplicationContext());
                            }

                        }
                    });
                }
            });
        } else {
            Log.d(LOG_TAG, "populateGridAdapter: There´s no Internet Connection!!");
        }
    }

    /**
     * populateGridAdapter Método que recebe a de reviews do filme e a coloca no RecyclerView
     *
     * @param videosList lista de reviews do filme a ser colocada no grid
     */
    private void populateVideosList(List<MovieVideos> videosList) {
        if (videosList != null) {
            if (videosList.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mMovieDetailsVideosRecycler.setLayoutManager(layoutManager);
                MovieVideosAdapter mMovieVideosAdapter = new MovieVideosAdapter(DetailsActivity.this, videosList);
                mMovieDetailsVideosRecycler.setAdapter(mMovieVideosAdapter);
            } else Log.d(LOG_TAG, "populateGridAdapter: Your search returned no Movies");
        } else Log.d(LOG_TAG, "populateGridAdapter: An error has occurred. Please try again");
    }


    private void testConnectionAndGetVideosData() {
        Boolean connected = NetworkUtilities.testConnection(getApplicationContext());
        if (connected) {
            final URL videosUrl = NetworkUtilities.createVideosReviewsUrl(String.valueOf(mMovieId), NetworkUtilities.VIDEOS);
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String reviewsJson = NetworkUtilities.getResponseFromHttpUrl(videosUrl);
                        movieVideosList = MovieDBUtilities.getMovieVideosFromJson(reviewsJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(LOG_TAG, "run: " + movieVideosList);
                            if (movieReviewsList != null) {
                                populateVideosList(movieVideosList);
                            }

                        }
                    });
                }
            });
        } else {
            Log.d(LOG_TAG, "populateGridAdapter: There´s no Internet Connection!!");
        }
    }

    private void openYoutubeIntent(String youtubeKey) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, MovieDBUtilities.getYoutubeVideoPath(youtubeKey, true));
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, MovieDBUtilities.getYoutubeVideoPath(youtubeKey, false));
            this.startActivity(webIntent);
        }

    }

    private void addRemoveFromFavorites() {

        mDb = AppDatabase.getInstance(getApplicationContext());

        final FavoriteMovies favorite = new FavoriteMovies(mMovieId);

        final MenuItem favoriteStar = mFavoriteStarMenu.findItem(R.id.action_favorite_star);

        if (MovieDBUtilities.checkIfMovieIsFavorite(mMovieId, getApplicationContext())) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.FavoritesDao().removeFavorite(favorite);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteStar.setIcon(R.drawable.baseline_star_border_white_36);
                        }
                    });
                }

            });
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.FavoritesDao().addNewFavorite(favorite);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteStar.setIcon(R.drawable.baseline_star_white_36);
                        }
                    });
                }
            });
        }

    }



}
