package com.example.android.project0_adndi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder> {

    final int POSTER_INT = 301;
    private final MovieGridAdapterOnClickHandler mClickHandler;
    private List <MovieData> mMovieList;
    private Context context;

    public MovieGridAdapter(MovieGridAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForGridItem = R.layout.movie_grid;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForGridItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieGridAdapter.MovieViewHolder viewHolder, int position) {
        String movieTitle = mMovieList.get( position ).getMovieName();
        String movieRanking = mMovieList.get( position ).getMovieRanking();
        String moviePosterURL = MovieDBUtilities.getFinalImageURL( mMovieList.get( position ).getMoviePosterURL(), POSTER_INT );

        viewHolder.mMovieTitleTextView.setText(movieTitle);
        viewHolder.mMovieRankingTextView.setText(movieRanking);
        viewHolder.mMovieRankingTextView.setTextColor(MovieDBUtilities.getScoreColor(Double.parseDouble(movieRanking)));

        ImageView posterImageView = viewHolder.mMoviePosterImageView;

        Picasso.with(context).load(moviePosterURL).into(posterImageView);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }

    public interface MovieGridAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public void setMovieData(List <MovieData> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final TextView mMovieTitleTextView;
        final TextView mMovieRankingTextView;
        final ImageView mMoviePosterImageView;

        MovieViewHolder(View view) {
            super(view);
            mMovieTitleTextView = view.findViewById(R.id.tv_list_movie_title);
            mMovieRankingTextView = view.findViewById(R.id.tv_list_movie_ranking);
            mMoviePosterImageView = view.findViewById(R.id.iv_list_movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int thisMovieId = mMovieList.get( adapterPosition ).getMovieId();
            mClickHandler.onClick(thisMovieId);
        }
    }
}