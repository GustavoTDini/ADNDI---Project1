package com.example.android.project0_adndi.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.FavoriteMovies;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = MoviesViewModel.class.getSimpleName();
    private LiveData<List<MovieData>> moviesDb;
    private LiveData<List<UrlMovieList>> urlDB;
    private LiveData<List<FavoriteMovies>> favoritesDB;

    public MoviesViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving all data from the DataBase");
        moviesDb = database.MovieDao().updateMovieDb();
        urlDB = database.MovieDao().updateMovieUrls();
        favoritesDB = database.MovieDao().loadFavorites();

    }

    public LiveData<List<MovieData>> getMovies() {
        return moviesDb;
    }

    public LiveData<List<UrlMovieList>> getUrlList() {
        return urlDB;
    }

    public LiveData<List<FavoriteMovies>> getFavorites() {
        return favoritesDB;
    }

}
