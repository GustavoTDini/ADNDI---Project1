package com.example.android.project0_adndi.MovieViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.MovieData;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private static final String TAG = MovieViewModel.class.getSimpleName();

    private LiveData<List<MovieData>> favoriteMovies;

    public MovieViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Atualizando os Favoritos");
        favoriteMovies = database.MovieDao().loadFavoriteMovies();
    }

    public LiveData<List<MovieData>> getFavoritesList() {
        return favoriteMovies;
    }
}
