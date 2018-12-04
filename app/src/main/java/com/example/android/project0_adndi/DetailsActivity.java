package com.example.android.project0_adndi;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    // String que conecta a MainActivity com este Intent atraves de PutExtra
    private static final String MOVIE_PARCEL = "movieParcel";

    // Código do Poster
    final int POSTER_INT = 301;

    // Código do BackDrop
    final int BACKDROP_INT = 302;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView mMovieDetailTitleTextView = findViewById(R.id.tv_details_movie_title);
        TextView mMovieDetailRankingTextView = findViewById(R.id.tv_details_movie_ranking);
        TextView mMovieDetailOverviewTextView = findViewById(R.id.tv_details_movie_overview);
        TextView mMovieDetailDateTextView = findViewById(R.id.tv_details_movie_date);
        ImageView mMovieDetailPosterImageView = findViewById(R.id.iv_details_movie_poster);
        ImageView mMovieDetailBackdropImageView = findViewById(R.id.iv_details_background);

        Intent intentThatStartedThisActivity = getIntent();

        //Verifica se o Intent tem o MOVIE_PARCEL extra
        if (intentThatStartedThisActivity.hasExtra(MOVIE_PARCEL)) {
            MovieData movie = intentThatStartedThisActivity.getParcelableExtra(MOVIE_PARCEL);
            //resgata os dados sobre o filme do MovieData movie
            String movieTitle = movie.getMovieName();
            String movieDate = movie.getMovieLaunchDate();
            String movieOverview = movie.getMovieOverView();
            String movieRanking = movie.getMovieRanking();
            String moviePosterURL = MovieDBUtilities.getFinalImageURL(movie.getMoviePosterURL(), POSTER_INT);
            String movieBackground = MovieDBUtilities.getFinalImageURL(movie.getMovieBackdropURL(), BACKDROP_INT);

            // Define as View com os Valores do MovieData movie
            Picasso.with(getBaseContext()).load(moviePosterURL).into(mMovieDetailPosterImageView);
            Picasso.with(getBaseContext()).load(movieBackground).into(mMovieDetailBackdropImageView);
            mMovieDetailDateTextView.setText(movieDate);
            mMovieDetailTitleTextView.setText(movieTitle);
            mMovieDetailOverviewTextView.setText(movieOverview);
            mMovieDetailRankingTextView.setText(movieRanking);
            int colorInt = MovieDBUtilities.getScoreColor(Double.parseDouble(movieRanking));
            int colorId = ContextCompat.getColor(this, colorInt);
            mMovieDetailRankingTextView.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);

        }

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
}
