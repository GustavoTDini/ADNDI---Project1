package com.example.android.project0_adndi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.ProjectUtilities.AppExecutors;
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

    // string com o query para a SEARCH
    String query = "";

    // string com o tipo selecionado - iniciado com POPULAR
    String selectedType = NetworkUtilities.POPULAR;

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
        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        // definição pelo findViewById do ProgressBar que indicará atividade de rede
        mProgressBar = findViewById(R.id.pb_loading_indicator);

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

        // inicia o AsynTask com um teste de concexão
        testConnectionGetAndSaveData(selectedType);

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
        detailsIntent.putExtra( MovieData.MOVIE_PARCEL, Parcels.wrap( movie ) );
        startActivity(detailsIntent);
    }

    /**
     * Metodo para a criação do Menu
     *
     * @param menu menu a ser criado
     */
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

    /**
     * Metodo para definirmos o que acontece com cada seleção do menu
     *
     * @param item item do menu selecionado
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Metodo selecionarmos quais view serão mostradas de acordo com a conexão ter dado erros ou não
     *
     * @param error     boolean para verificar se houve erro
     * @param errorText caso tenha ocorrido um erro mostrará esta mensagem em mErrorMessageDisplay
     */
    private void showViews(Boolean error, String errorText) {
        if (error) {
            Log.v(TAG, "ShowViews " + error.toString());
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText(errorText);
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
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
                MovieGridAdapter mMovieGridAdapter = new MovieGridAdapter(MainActivity.this);
                mMovieGridAdapter.setMovieData(movieList);
                mRecyclerView.setAdapter(mMovieGridAdapter);
                mScrollView.scrollTo(0, 0);
                showHideButtons(currentPage);
                mProgressBar.setVisibility(View.INVISIBLE);
                showViews(false, null);
            } else showViews(true, "Your search returned no Movies");
        } else showViews(true, "An error has occurred. Please try again");
    }


    private void testConnectionGetAndSaveData(String type) {
        Boolean connected = NetworkUtilities.testConnection(getApplicationContext());
        if (connected) {
            currentUrl = NetworkUtilities.createCurrentUrl(type, currentPage, query);
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
                            Log.d(TAG, "run: " + movieDataList);
                            populateGridAdapter(movieDataList);
                            MovieDBUtilities.saveMoviesDataToDB(movieDataList, getApplicationContext());
                        }
                    });
                }
            });
            MovieDBUtilities.createMovieListFromUrl(currentJson, String.valueOf(currentPage), getApplicationContext());
        } else {
            showViews(true, "There´s no Internet Connection!!");
        }
    }


//    private void getMovieDatafromDB(final String JsonString){
//
//        mDb = AppDatabase.getInstance(getApplicationContext());
//
//        final String urlString = currentUrl.toString();
//
//        if (JsonString != null) {
//            currentPage = MovieDBUtilities.extractTotalPages(JsonString);
//            AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    UrlMovieList movieListUrl = mDb.MovieDao().loadMovieUrlList(urlString, currentPage);
//                    if (movieListUrl != null) {
//                        String JsonListString = movieListUrl.getMoviesList();
//                        List<MovieData> movieList = MovieDBUtilities.getFilmListArrayFromDb(JsonListString, getApplicationContext());
//                        Log.d(TAG, "run: " + movieList);
//                        populateGridAdapter(movieList);
//                    } else {
//                        MovieDBUtilities.createMovieListFromUrl(JsonString, urlString, getApplicationContext());
//                        List<MovieData> movieList = MovieDBUtilities.getFilmListArrayFromDb(urlString, getApplicationContext());
//                        populateGridAdapter(movieList);
//                    }
//                }
//            });
//
//
//
//
//        } else showViews( true, "An error has occurred. Please try again" );
//
//    }

    private boolean checkIfDataExistsInDB(final String JsonString, final int page) {

        // Criei está variável como uma array para poder modificar o valor mesmo sendo final
        final Boolean[] isDataInDbBoolean = {false};

        mDb = AppDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                isDataInDbBoolean[0] = mDb.MovieDao().checkIfRequestExists(JsonString, page);

            }
        });

        return isDataInDbBoolean[0];
    }

}

