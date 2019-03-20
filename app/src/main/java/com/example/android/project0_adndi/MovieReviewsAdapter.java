package com.example.android.project0_adndi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.MovieReviews;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewsViewHolder> {

    //  TAG desta Classe - para os erros
    private final String TAG = MovieReviewsAdapter.class.getSimpleName();

    // Lista dos filmes codificada em MovieReviews
    private List<MovieReviews> mMovieReviews;

    /**
     * Construtor da classe
     */
    MovieReviewsAdapter(List<MovieReviews> movieReviews) {
        mMovieReviews = movieReviews;
    }

    /**
     * onCreateViewHolder, cria os varios viewHolders que irão ser inflados
     *
     * @param viewGroup o ViewGroup que contem esta viewHolder
     * @param i         tipo do view utilizado, neste caso não é utilizado, pois só temos 1 tipo de view
     * @return o ViewHolder criado
     */
    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // contexto atual
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reviews_list, viewGroup, false);
        return new ReviewsViewHolder(view);
    }

    /**
     * onBindViewHolder, povoa os ReviewsViewHolder com as Informações dos Reviews
     *
     * @param viewHolder viewHolder a ser povoado, neste caso um MovieViewHOlder
     * @param position   posição do MovieRevies da list
     */
    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder viewHolder, int position) {
        String reviewAuthor = mMovieReviews.get(position).getReviewAuthor();
        String reviewContent = mMovieReviews.get(position).getReviewContent();

        viewHolder.mReviewAuthor.setText(reviewAuthor);
        viewHolder.mReviewContent.setText(reviewContent);
    }

    /**
     * getItemCount, retorna o tamanho da lista
     *
     * @return int com o tamanho
     */
    @Override
    public int getItemCount() {
        if (null == mMovieReviews) return 0;
        return mMovieReviews.size();
    }

    /**
     * Classe de viewHolder para definirmos os views a serem utilizados
     */
    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        final TextView mReviewAuthor;
        final TextView mReviewContent;

        ReviewsViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.tv_reviews_author);
            mReviewContent = view.findViewById(R.id.tv_reviews_content);
        }

    }
}
