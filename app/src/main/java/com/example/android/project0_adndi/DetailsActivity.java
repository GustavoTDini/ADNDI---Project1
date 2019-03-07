package com.example.android.project0_adndi;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

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
        if (intentThatStartedThisActivity.hasExtra( MovieData.MOVIE_PARCEL )) {
            MovieData movie = Parcels.unwrap( getIntent().getParcelableExtra( MovieData.MOVIE_PARCEL ) );
            //resgata os dados sobre o filme do MovieData movie
            String movieTitle = movie.getMovieName();
            String movieDate = movie.getMovieLaunchDate();
            String movieOverview = movie.getMovieOverView();
            String movieRanking = movie.getMovieRanking();
            String moviePosterDirPath = movie.getMoviePosterDirPath();
            String movieBackgroundDirPath = movie.getMovieBackdropDirPath();

            // Define as View com os Valores do MovieData movie
            Picasso.with(getBaseContext()).load(moviePosterDirPath).into(mMovieDetailPosterImageView);
            Picasso.with(getBaseContext()).load(movieBackgroundDirPath).into(mMovieDetailBackdropImageView);
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
