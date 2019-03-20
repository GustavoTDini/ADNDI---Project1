package com.example.android.project0_adndi;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    RecyclerView mMovieDetailsVideosRecycler;
    TextView mVideosErrorMessageDisplay;
    ProgressBar mVideosProgressBar;

    RecyclerView mMovieDetailsReviewsRecycler;
    TextView mReviewsErrorMessageDisplay;
    ProgressBar mReviewsProgressBar;

    int mMovieId = 0;

    MenuItem mFavoriteStar;

    FavoriteMovies mFavoriteMovie;

    // Iniciação da DataBase
    private static AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDb = AppDatabase.getInstance(getApplicationContext());

        TextView mMovieDetailTitleTextView = findViewById(R.id.tv_details_movie_title);
        TextView mMovieDetailRankingTextView = findViewById(R.id.tv_details_movie_ranking);
        TextView mMovieDetailOverviewTextView = findViewById(R.id.tv_details_movie_overview);
        TextView mMovieDetailDateTextView = findViewById(R.id.tv_details_movie_date);
        TextView mMovieDetailGenreTextView = findViewById(R.id.tv_details_movie_genres);
        ImageView mMovieDetailPosterImageView = findViewById(R.id.iv_details_movie_poster);
        ImageView mMovieDetailBackdropImageView = findViewById(R.id.iv_details_background);
        mMovieDetailsReviewsRecycler = findViewById(R.id.rv_review_list);
        mMovieDetailsVideosRecycler = findViewById(R.id.rv_videos_list);
        mReviewsErrorMessageDisplay = findViewById(R.id.tv_reviews_error_message_display);
        mVideosErrorMessageDisplay = findViewById(R.id.tv_videos_error_message_display);
        mReviewsProgressBar = findViewById(R.id.pb_reviews_loading_indicator);
        mVideosProgressBar = findViewById(R.id.pb_videos_loading_indicator);


        Intent intentThatStartedThisActivity = getIntent();

        //Verifica se o Intent tem o MOVIE_PARCEL extra
        if (intentThatStartedThisActivity.hasExtra( MovieData.MOVIE_PARCEL )) {
            MovieData movie = Parcels.unwrap( getIntent().getParcelableExtra( MovieData.MOVIE_PARCEL ) );
            Log.d(LOG_TAG, "onCreate: " + movie);
            //resgata os dados sobre o filme do MovieData movie
            if (movie != null) {
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


        }
        setFavorite();
        testConnectionAndGetReviewsData();
        testConnectionAndGetVideosData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_activity_menu, menu);
        mFavoriteStar = menu.findItem(R.id.action_favorite_star);

        observeFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite_star) {
            addRemoveFromFavorites(mFavoriteMovie);
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

    /**
     * Metodo que override o onclick da RecyclerView do video para abrirmos uma intent para mostrar o
     * video seja no app do youtube ou no browser
     *
     * @param video MovieVideos a ser mostrado no intent
     */
    @Override
    public void onClick(MovieVideos video) {
        openYoutubeIntent(video.getVideoKey());
    }

    /**
     * populateReviewsList Método que recebe a de reviews do filme e a coloca no RecyclerView
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
            } else
                showViews(true, "Your search returned no Reviews", mReviewsErrorMessageDisplay, mMovieDetailsReviewsRecycler, mReviewsProgressBar);
        } else
            showViews(true, "An error has occurred. Please try again", mReviewsErrorMessageDisplay, mMovieDetailsReviewsRecycler, mReviewsProgressBar);
    }

    /**
     * testConnectionAndGetReviewsData Método que testa a conexão, busca os review do filme mostrado,
     * caso não haja conexão, mostra uma mensagem
     */
    private void testConnectionAndGetReviewsData() {
        Boolean connected = NetworkUtilities.testConnection(getApplicationContext());
        if (connected) {
            mReviewsProgressBar.setVisibility(View.VISIBLE);
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
                                showViews(false, null, mReviewsErrorMessageDisplay, mMovieDetailsReviewsRecycler, mReviewsProgressBar);
                            }

                        }
                    });
                }
            });
        } else {
            showViews(true, "There´s no Internet Connection!!", mReviewsErrorMessageDisplay, mMovieDetailsReviewsRecycler, mReviewsProgressBar);
        }
    }

    /**
     * populateGridAdapter Método que recebe a lista de videos do filme e a coloca no RecyclerView
     *
     * @param videosList lista de videos do filme a ser colocada no grid
     */
    private void populateVideosList(List<MovieVideos> videosList) {
        if (videosList != null) {
            if (videosList.size() > 0) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mMovieDetailsVideosRecycler.setLayoutManager(layoutManager);
                MovieVideosAdapter mMovieVideosAdapter = new MovieVideosAdapter(DetailsActivity.this, videosList);
                mMovieDetailsVideosRecycler.setAdapter(mMovieVideosAdapter);
            } else
                showViews(true, "Your search returned no Videos", mVideosErrorMessageDisplay, mMovieDetailsVideosRecycler, mVideosProgressBar);
        } else
            showViews(true, "An error has occurred. Please try again", mVideosErrorMessageDisplay, mMovieDetailsVideosRecycler, mVideosProgressBar);
    }

    /**
     * testConnectionAndGetVideosData Método que testa a conexão, busca os videos do filme mostrado,
     * caso não haja conexão, mostra uma mensagem
     */
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
                                showViews(false, null, mVideosErrorMessageDisplay, mMovieDetailsVideosRecycler, mVideosProgressBar);
                            }

                        }
                    });
                }
            });
        } else {
            showViews(true, "There´s no Internet Connection!!", mVideosErrorMessageDisplay, mMovieDetailsVideosRecycler, mVideosProgressBar);
        }
    }

    /**
     * Metodo que é chamado quando se clica no videothumbnail, caso houver o app do Youtube,
     * abre nesse app, caso contrario, abre no Browser
     *
     * @param youtubeKey key do video a ser aberto no intent
     */
    private void openYoutubeIntent(String youtubeKey) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, NetworkUtilities.getYoutubeVideoPath(youtubeKey, true));
            this.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, NetworkUtilities.getYoutubeVideoPath(youtubeKey, false));
            this.startActivity(webIntent);
        }

    }

    /**
     * Metodo que inverte a seleção de favorito do filme
     *
     * @param currentFavorite o FavoriteMovies do atual filme mostrado
     */
    private void addRemoveFromFavorites(final FavoriteMovies currentFavorite) {
        currentFavorite.setMovieFavorite(!currentFavorite.getMovieFavorite());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.FavoritesDao().updateFavorite(currentFavorite);
            }
        });

    }

    /**
     * Metodo que muda a estrela de favoritos, caso o filme seja ou não selecionado como favorito
     *
     * @param favorite Boolean com a informação se o filme é ou não favorito
     */
    private void updateFavoritesStarIcon(Boolean favorite) {
        if (favorite) {
            mFavoriteStar.setIcon(R.drawable.baseline_star_white_36);
        } else {
            mFavoriteStar.setIcon(R.drawable.baseline_star_border_white_36);
        }
    }

    /**
     * Metodo selecionarmos quais view serão mostradas de acordo com a conexão ter dado erros ou não
     *
     * @param error         boolean para verificar se houve erro
     * @param errorText     caso tenha ocorrido um erro mostrará esta mensagem em mErrorMessageDisplay
     * @param errorTextView view que será mostrado o erro
     * @param recyclerView  recyclerview a ser populado
     * @param progressBar   progressbar de carrgamento de dados
     */
    public void showViews(Boolean error, String errorText, TextView errorTextView, RecyclerView recyclerView, ProgressBar progressBar) {
        if (error) {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(errorText);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            errorTextView.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Metodo que cria um objeto favorito, caso ainda não exista no DB
     */
    private void setFavorite() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (!mDb.FavoritesDao().checkIfFavoriteExists(mMovieId)) {
                    FavoriteMovies favoriteToAdd = new FavoriteMovies(mMovieId);
                    mDb.FavoritesDao().addNewFavorite(favoriteToAdd);
                }
            }
        });
    }

    /**
     * Metodo que cria um observador LiveData no objeto FavoriteMovie Salvo no DB relativo ao filme atual
     */
    private void observeFavorite() {
        LiveData<FavoriteMovies> favoriteData = mDb.FavoritesDao().loadFavoriteById(mMovieId);
        favoriteData.observe(this, new Observer<FavoriteMovies>() {
            @Override
            public void onChanged(@Nullable FavoriteMovies movies) {
                if (movies != null) {
                    mFavoriteMovie = movies;
                    updateFavoritesStarIcon(mFavoriteMovie.getMovieFavorite());
                }
            }
        });
    }

}
