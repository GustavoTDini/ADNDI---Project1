package com.example.android.project0_adndi;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    //  TAG desta Classe - para os erros
    private final String TAG = MovieGridAdapter.class.getSimpleName();

    // ClickHandler para definirmos a interface
    private final MovieGridAdapterOnClickHandler mClickHandler;

    // Lista dos filmes codificada em MovieData
    private List <MovieData> mMovieList;

    // contexto atual
    private Context context;

    /**
     * Construtor da classe
     *
     * @param clickHandler interface para definirmos a função do click
     */
    MovieGridAdapter(MovieGridAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * onCreateViewHolder, cria os varios viewHolders que irão
     *
     * @param viewGroup o ViewGroup que contem esta viewHolder
     * @param i         tipo do view utilizado, neste caso não é utilizado, pois só temos 1 tipo de view
     * @return o ViewHolder criado
     */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_grid, viewGroup, false);
        return new MovieViewHolder(view);
    }

    /**
     * onBindViewHolder, povoa os MovieViewHolder com as Informações do MovieData
     *
     * @param viewHolder viewHolder a ser povoado, neste caso um MovieViewHOlder
     * @param position   posição do MovieData da list
     */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int position) {
        Context thisContext = this.context;
        String movieTitle = mMovieList.get( position ).getMovieName();
        String movieRanking = mMovieList.get( position ).getMovieRanking();
        int POSTER_INT = 301;
        String moviePosterURL = MovieDBUtilities.getFinalImageURL(mMovieList.get(position).getMoviePosterURL(), POSTER_INT);

        viewHolder.mMovieTitleTextView.setText(movieTitle);
        viewHolder.mMovieRankingTextView.setText(movieRanking);
        int colorInt = MovieDBUtilities.getScoreColor(Double.parseDouble(movieRanking));
        int colorId = ContextCompat.getColor(thisContext, colorInt);
        viewHolder.mMovieRankingTextView.getBackground().setColorFilter(colorId, PorterDuff.Mode.SRC_ATOP);

        Log.v(TAG, String.valueOf(colorInt));

        ImageView posterImageView = viewHolder.mMoviePosterImageView;

        Picasso.with(context).load(moviePosterURL).into(posterImageView);

    }

    /**
     * getItemCount, retorna o tamanho da lista
     *
     * @return int com o tamanho
     */
    @Override
    public int getItemCount() {
        if (null == mMovieList) return 0;
        return mMovieList.size();
    }

    /**
     * setMovieData define qual lista queremos usar no MovieGridAdapter
     *
     * @param movieList lista que utilizaremos
     */
    void setMovieData(List <MovieData> movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    /**
     * Interface que define o onclik da View
     */
    public interface MovieGridAdapterOnClickHandler {
        void onClick(MovieData movie);
    }

    /**
     * Classe de viewHolder para definirmos os views a serem utilizados
     */
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

        /**
         * Método a ser chamado em um click em uma view
         *
         * @param v view que foi clicada
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieData thisMovie = mMovieList.get(adapterPosition);
            mClickHandler.onClick(thisMovie);
        }
    }
}