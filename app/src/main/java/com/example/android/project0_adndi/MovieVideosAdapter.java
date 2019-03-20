package com.example.android.project0_adndi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.MovieVideos;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieVideosAdapter extends RecyclerView.Adapter<MovieVideosAdapter.VideosViewHolder> {

    //  TAG desta Classe - para os erros
    private final String TAG = MovieVideosAdapter.class.getSimpleName();

    // ClickHandler para definirmos a interface
    private final MovieVideosAdapter.MovieVideosAdapterOnClickHandler mClickHandler;

    // Lista dos filmes codificada em MovieData
    private List<MovieVideos> mVideosList;

    // contexto atual
    private Context context;

    /**
     * Construtor da classe
     *
     * @param clickHandler interface para definirmos a função do click
     */
    MovieVideosAdapter(MovieVideosAdapter.MovieVideosAdapterOnClickHandler clickHandler, List<MovieVideos> videosList) {
        mVideosList = videosList;
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
    public MovieVideosAdapter.VideosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_list, viewGroup, false);
        return new MovieVideosAdapter.VideosViewHolder(view);
    }

    /**
     * onBindViewHolder, povoa os MovieViewHolder com as Informações do MovieData
     *
     * @param viewHolder viewHolder a ser povoado, neste caso um MovieViewHOlder
     * @param position   posição do MovieData da list
     */
    @Override
    public void onBindViewHolder(@NonNull MovieVideosAdapter.VideosViewHolder viewHolder, int position) {
        String videoTitle = mVideosList.get(position).getVideoTitle();
        String videoKey = mVideosList.get(position).getVideoKey();

        viewHolder.mVideoTitleTextView.setText(videoTitle);
        ImageView thumbnailImageView = viewHolder.mVideoThumbnailImageView;

        Picasso.with(context).load(NetworkUtilities.getYoutubeThumbnailPath(videoKey)).into(thumbnailImageView);

    }

    /**
     * getItemCount, retorna o tamanho da lista
     *
     * @return int com o tamanho
     */
    @Override
    public int getItemCount() {
        if (null == mVideosList) return 0;
        return mVideosList.size();
    }

    /**
     * Interface que define o onclik da View
     */
    public interface MovieVideosAdapterOnClickHandler {
        void onClick(MovieVideos videos);
    }

    /**
     * Classe de viewHolder para definirmos os views a serem utilizados
     */
    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView mVideoTitleTextView;
        final ImageView mVideoThumbnailImageView;

        VideosViewHolder(View view) {
            super(view);
            mVideoTitleTextView = view.findViewById(R.id.tv_videos_title);
            mVideoThumbnailImageView = view.findViewById(R.id.iv_videos_thumbnail);
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
            MovieVideos video = mVideosList.get(adapterPosition);
            mClickHandler.onClick(video);
        }
    }

}
