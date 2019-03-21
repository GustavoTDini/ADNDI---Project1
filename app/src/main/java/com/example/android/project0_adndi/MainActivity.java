package com.example.android.project0_adndi;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.FavoriteMovies;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;
import com.example.android.project0_adndi.ProjectUtilities.AppExecutors;
import com.example.android.project0_adndi.ProjectUtilities.DataBaseUtilities;
import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieGridAdapterOnClickHandler {

    //  TAG desta Classe - para os erros
    private static final String TAG = MainActivity.class.getSimpleName();

    // int com o total de View do grid em Modo paisagem
    private static final int LANDSCAPE_SPAM = 3;

    // int com o total de View do grid em Modo retrato
    private static final int PORTRAIT_SPAM = 2;

    private static final String SAVED_TYPE = "selected_type";
    private static final String SAVED_PAGE = "selected_page";

    private static final String FAVORITES = "0110";

    // int com o numero de paginas atual
    int currentPage = 1;

    // int com o numero de paginas totais
    int totalPages = 2;

    // Iniciação da DataBase
    private static AppDatabase mDb;

    //url a ser utilizado
    URL currentUrl;

    String currentJson;

    List<MovieData> movieDataList;

    List<MovieData> favoritesList;

    // string com o query para a SEARCH
    String query = "";

    // string com o tipo selecionado
    String selectedType;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private Button mAddPagesButton;
    private Button mDecreasePagesButton;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme para utilizarmos o Splash screen até tudo ser carregado
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // definição pelo findViewById do ReciclerView que mostrará os Filmes
        mRecyclerView = findViewById(R.id.rv_movie_list);

        // definição pelo findViewById do Texview que mostrará as mensagem de erro
        mErrorMessageDisplay = findViewById(R.id.tv_reviews_error_message_display);

        // definição pelo findViewById do ProgressBar que indicará atividade de rede
        mProgressBar = findViewById(R.id.pb_reviews_loading_indicator);

        // definição pelo findViewById do Button que aumenta 1 pagina
        mAddPagesButton = findViewById(R.id.bt_page_up);

        // definição pelo findViewById do Button que diminui 1 pagina
        mDecreasePagesButton = findViewById(R.id.bt_page_down);

        mScrollView = findViewById(R.id.sv_main_activity);

        // ClickListener de mAddPagesButton utiliza o metodo addDecreasePages
        mAddPagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDecreasePages(true);
            }
        });

        // ClickListener de mDecreasePagesButton utiliza o metodo addDecreasePages
        mDecreasePagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDecreasePages(false);
            }
        });

        // Recebimento da orientação e definição do gridSpam para a correta exibição da View
        int orientation = getResources().getConfiguration().orientation;
        int gridSpam = PORTRAIT_SPAM;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridSpam = LANDSCAPE_SPAM;
        }

        // definição e inicialização do GridLayoutManager com o MovieGridAdapter no mRecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, gridSpam);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            selectedType = savedInstanceState.getString(SAVED_TYPE);
            currentPage = savedInstanceState.getInt(SAVED_PAGE);
        } else {
            selectedType = NetworkUtilities.POPULAR;
            currentPage = 1;
        }

        observeFavorites();

        if (!selectedType.equals(FAVORITES)) {
            testConnectionGetAndSaveData(selectedType);
        } else {
            showFilmFavoritesList();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.sv_search_movie);

        SearchView searchView = (SearchView) searchItem.getActionView();

        // setOnQueryTextListener da searchView de modo a definirmos o que
        // acontece ao passarmos uma informação a ela
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                doMySearch(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        currentPage = 1;

        switch (id) {
            case R.id.action_popular:
                testConnectionGetAndSaveData(NetworkUtilities.POPULAR);
                return true;
            case R.id.action_top_rated:
                testConnectionGetAndSaveData(NetworkUtilities.TOP_RATED);
                return true;
            case R.id.action_upcoming:
                testConnectionGetAndSaveData(NetworkUtilities.UPCOMING);
                return true;
            case R.id.action_now_playing:
                testConnectionGetAndSaveData(NetworkUtilities.NOW_PLAYING);
                return true;
            case R.id.action_favorites:
                showFilmFavoritesList();
                selectedType = FAVORITES;
                return true;
            case R.id.action_empty_cache:
                deleteAllCache();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_TYPE, selectedType);
        outState.putInt(SAVED_PAGE, currentPage);
    }

    /**
     * Metodo que override o onclick da View para abrirmos uma intent de
     * DetailsActivity e passarmos os dados de movie
     *
     * @param movie MovieData a ser passada para outra intent
     */
    @Override
    public void onClick(MovieData movie) {
        Context context = this;
        Class destinationClass = DetailsActivity.class;
        Intent detailsIntent = new Intent(context, destinationClass);
        detailsIntent.putExtra(MovieData.MOVIE_PARCEL, Parcels.wrap(movie));
        startActivity(detailsIntent);
    }

    /**
     * Metodo selecionarmos quais view serão mostradas de acordo com a conexão ter dado erros ou não
     *
     * @param error     boolean para verificar se houve erro
     * @param errorText caso tenha ocorrido um erro mostrará esta mensagem em mErrorMessageDisplay
     */
    public void showViews(Boolean error, String errorText) {
        Log.d(TAG, "ShowViews: " + error.toString());
        if (error) {
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(errorText);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * doMySearch Metodo que fará uma nova busca de acordo com query
     *
     * @param searchQuery string a ser buscada em SEARCH
     */
    private void doMySearch(String searchQuery) {
        query = searchQuery;
        testConnectionGetAndSaveData(NetworkUtilities.SEARCH);
    }

    /**
     * addDecreasePages Método para adicionar ou remover 1 pagina e
     * fazer uma nova busca com a pagina selecionada
     *
     * @param add booleano se true significa adicionar, caso contrario subtrair
     */
    private void addDecreasePages(boolean add) {
        if (add) {
            if (currentPage < totalPages) currentPage++;
        } else {
            if (currentPage != 1) currentPage--;
        }
        testConnectionGetAndSaveData(selectedType);
    }

    /**
     * showHideButtons Método para definir a visibilidade dos botões de controle de paginas
     *
     * @param pages pagina atual da busca
     */
    private void showHideButtons(int pages) {
        mAddPagesButton.setVisibility(View.VISIBLE);
        mDecreasePagesButton.setVisibility(View.VISIBLE);
        if (pages == 1) {
            mDecreasePagesButton.setVisibility(View.GONE);
        }
        if (pages == totalPages) {
            mAddPagesButton.setVisibility(View.GONE);
        }
    }

    /**
     * populateGridAdapter Método que recebe a lista de filmes e a coloca no RecyclerView
     *
     * @param movieList lista de filmes a ser colocada no grid
     */
    private void populateGridAdapter(List<MovieData> movieList) {
        Log.d(TAG, "populateGridAdapter: " + movieList);
        if (movieList != null) {
            if (movieList.size() > 0) {
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.removeAllViewsInLayout();
                MovieGridAdapter mMovieGridAdapter = new MovieGridAdapter(MainActivity.this);
                mMovieGridAdapter.setMovieData(movieList);
                mRecyclerView.setAdapter(mMovieGridAdapter);
                mScrollView.scrollTo(0, 0);
                showHideButtons(currentPage);
                showViews(false, null);
            } else showViews(true, "Your search returned no Movies");
        } else showViews(true, "An error has occurred. Please try again");
    }

    /**
     * testConnectionGetAndSaveData - Principal método desta view, define a Url a realizar a busca,
     * testa se há conexão, em caso positivo, carrega todos os filmes da busca, os grava no DB, e salva
     * o Array com os filmes em URLMovieList em caso negativo, testa se já existe essa URl e paginas salvas, se estiver, as mostra,
     * caso contrario, mostra a mensagem de que não ha conexão
     *
     * @param type String com o tipo da busca da ser realizada
     */
    private void testConnectionGetAndSaveData(String type) {
        Boolean connected = NetworkUtilities.testConnection(getApplicationContext());
        mRecyclerView.removeAllViewsInLayout();
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.GONE);
        selectedType = type;
        currentUrl = NetworkUtilities.createCurrentUrl(type, currentPage, query);

        if (connected) {
            AppExecutors.getInstance().networkIO().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        currentJson = NetworkUtilities.getResponseFromHttpUrl(currentUrl);
                        totalPages = MovieDBUtilities.extractTotalPages(currentJson);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    movieDataList = MovieDBUtilities.getMovieDataFromJson(currentJson);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateGridAdapter(movieDataList);
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    DataBaseUtilities.saveMoviesDataToDB(movieDataList, getApplicationContext());
                    DataBaseUtilities.createMovieListFromUrl(currentJson, currentUrl.toString(), getApplicationContext());
                }
            });

        } else AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (DataBaseUtilities.checkIfUrlExists(currentUrl.toString(), currentPage, getApplicationContext())) {
                    movieDataList = DataBaseUtilities.getMovieDataFromDB(currentUrl.toString(), currentPage, getApplicationContext());
                    Log.d(TAG, "run: " + movieDataList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (movieDataList != null) populateGridAdapter(movieDataList);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showViews(true, "There´s no Internet Connection!!");
                        }
                    });
                }
            }
        });
    }

    /**
     * deleteAllCache Método que apaga todos os dados, devido a ser uma exceção, para confirmae se o usuario realmente
     * quer fazer isso, é aberto um dialogo para confirmar
     */
    private void deleteAllCache() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Data Exclusion")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final AppDatabase mDb = AppDatabase.getInstance(getApplicationContext());
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.FavoritesDao().emptyFavorites();
                                mDb.MovieDao().emptyMovieCache();
                                mDb.UrlDao().emptyUrlListCache();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRecyclerView.removeAllViewsInLayout();
                                    }
                                });
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();

    }

    /**
     * showFilmFavoritesList - método que carrega a lista de favoritos a mostra no recyclerView
     */
    private void showFilmFavoritesList() {

        mErrorMessageDisplay.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.removeAllViewsInLayout();
        populateGridAdapter(favoritesList);
        mAddPagesButton.setVisibility(View.GONE);
        mDecreasePagesButton.setVisibility(View.GONE);

    }

    /**
     * observeFavorites - método que atualiza o favorites atraves do Livedata e veificando se o query atual é de favoritos, atualiza a
     */
    private void observeFavorites() {

        mDb = AppDatabase.getInstance(getApplicationContext());

        final LiveData<List<MovieData>> favoriteList = mDb.MovieDao().loadFavoriteMovies();

        favoriteList.observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                favoritesList = movieData;
                if (selectedType.equals(FAVORITES)) {
                    showFilmFavoritesList();
                }
                Log.d(TAG, "onChanged: " + movieData);
            }
        });

    }

    /**
     * showDataBase - método utilizado para debug que mostra todos os bancos de dados
     */
    private void showDataBase() {
        mDb = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<List<FavoriteMovies>> favoritesMovieList = mDb.FavoritesDao().loadFavorites();
                List<MovieData> MovieList = mDb.MovieDao().loadMovies();
                List<UrlMovieList> UrlList = mDb.UrlDao().loadUrlList();
                Log.d(TAG, "showDataBase: Movies - " + MovieList.toString());
                Log.d(TAG, "showDataBase: Favorites - " + favoritesMovieList.toString());
                Log.d(TAG, "showDataBase: URl - " + UrlList.toString());
            }
        });
    }
}

