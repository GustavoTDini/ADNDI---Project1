package com.example.android.project0_adndi;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.android.project0_adndi.DataUtilities.AppDatabase;
import com.example.android.project0_adndi.DataUtilities.MovieData;
import com.example.android.project0_adndi.DataUtilities.UrlMovieList;
import com.example.android.project0_adndi.ProjectUtilities.MovieDBUtilities;
import com.example.android.project0_adndi.ProjectUtilities.NetworkUtilities;

import org.parceler.Parcels;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieGridAdapter.MovieGridAdapterOnClickHandler, AsyncTaskDelegate {

    //  TAG desta Classe - para os erros
    private static final String TAG = MainActivity.class.getSimpleName();

    // int com o total de View do grid em Modo paisagem
    private static final int LANDSCAPE_SPAM = 3;

    // int com o total de View do grid em Modo retrato
    private static final int PORTRAIT_SPAM = 2;
    // int com o numero de paginas atual
    int jsonPages = 1;

    // int com o numero de paginas totais
    int totalPages = 2;

    // Iniciação da DataBase
    private static AppDatabase mDb;
    URL currentUrl;

    // string com o query para a SEARCH
    String query = "";
    // string com o tipo selecionado - iniciado com POPULAR
    String selectedType = NetworkUtilities.POPULAR;

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    //    private Button mAddPagesButton;
//    private Button mDecreasePagesButton;
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

//        // definição pelo findViewById do Button que aumenta 1 pagina
//        mAddPagesButton = findViewById(R.id.bt_page_up);
//
//        // definição pelo findViewById do Button que diminui 1 pagina
//        mDecreasePagesButton = findViewById(R.id.bt_page_down);

        mScrollView = findViewById( R.id.sv_main_activity );

//        // ClickListener de mAddPagesButton utiliza o metodo addDecreasePages
//        mAddPagesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addDecreasePages(true);
//            }
//        });
//
//        // ClickListener de mDecreasePagesButton utiliza o metodo addDecreasePages
//        mDecreasePagesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addDecreasePages(false);
//            }
//        });


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
        testConnectionAndStartAsync(selectedType);

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
                testConnectionAndStartAsync( NetworkUtilities.POPULAR );
                return true;
            case R.id.action_top_rated:
                testConnectionAndStartAsync( NetworkUtilities.TOP_RATED );
                return true;
            case R.id.action_upcoming:
                testConnectionAndStartAsync( NetworkUtilities.UPCOMING );
                return true;
            case R.id.action_now_playing:
                testConnectionAndStartAsync( NetworkUtilities.NOW_PLAYING );
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
     * testConnectionAndStartAsync Metodo que testará ser a conexão está ativa e em caso positivo
     * inicia o AsyncMovieTask
     *
     * @param type tipo de busca a ser realizada
     */
    private void testConnectionAndStartAsync(String type) {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        mProgressBar.setVisibility( View.VISIBLE );
        mErrorMessageDisplay.setVisibility( View.INVISIBLE );
        mRecyclerView.setVisibility( View.INVISIBLE );
        if (networkInfo != null && networkInfo.isConnected()) {
            String stringPages = String.valueOf( jsonPages );
            selectedType = type;
            if (selectedType.equals( NetworkUtilities.SEARCH )) {
                currentUrl = NetworkUtilities.buildMovieSearchURL(selectedType, query, stringPages);
            } else {
                currentUrl = NetworkUtilities.buildMovieSearchURL(selectedType, null, stringPages);
            }
            new AsyncMovieTask(new AsyncTaskDelegate() {
                @Override
                public void processFinish(String output) {
                    finishAsyncProcess(output);
                }
            }).execute(currentUrl);
//            showhideButtons( jsonPages );
        } else {
            showViews(true, "There´s no Internet Connection!!");
        }
    }

    /**
     * doMySearch Metodo que fará uma nova busca de acordo com query
     *
     * @param searchQuery string a ser buscada em SEARCH
     */
    private void doMySearch(String searchQuery) {
        query = searchQuery;
        testConnectionAndStartAsync( NetworkUtilities.SEARCH );
    }

    /**
     * addDecreasePages Método para adicionar ou remover 1 pagina e
     * fazer uma nova busca com a pagina selecionada
     *
     * @param add booleano se true significa adicionar, caso contrario subtrair
     */
    private void addDecreasePages(boolean add) {
        if (add) {
            if (jsonPages < totalPages) jsonPages++;
        } else {
            if (jsonPages != 1) jsonPages--;
        }
        testConnectionAndStartAsync(selectedType);
    }

//    /**
//     * showhideButtons Método para definir a visibilidade dos botões de controle de paginas
//     */
//    private void showhideButtons(int pages) {
//        mAddPagesButton.setVisibility( View.VISIBLE );
//        mDecreasePagesButton.setVisibility( View.VISIBLE );
//        if (pages == 1) {
//            mDecreasePagesButton.setVisibility(View.GONE);
//        }
//        if (pages == totalPages) {
//            mAddPagesButton.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void processFinish(String output) {
        finishAsyncProcess( output );
    }

    public void finishAsyncProcess(final String jsonResponse) {


        mDb = AppDatabase.getInstance(getApplicationContext());

        final String urlString = currentUrl.toString();

        // retorna apenas se a resposta for válido
        if (jsonResponse != null) {
            jsonPages = MovieDBUtilities.extractPage(jsonResponse);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    LiveData<UrlMovieList> movieListUrlLiveData = mDb.MovieDao().checkIfRequestExists(urlString, jsonPages);
                    Log.d(TAG, "run: " + movieListUrlLiveData);
                    if (movieListUrlLiveData != null) {
                        UrlMovieList movieListsUrl = movieListUrlLiveData.getValue();
                        Log.d(TAG, "run: " + movieListsUrl);
                        String JsonListString = movieListsUrl.getMoviesList();
                        List<MovieData> movieList = MovieDBUtilities.getFilmListArrayFromDb(JsonListString, getApplicationContext());
                        populateGridAdapter(movieList);
                    } else {
                        MovieDBUtilities.createMovieListFromUrl(jsonResponse, urlString, getApplicationContext());
                        List<MovieData> movieList = MovieDBUtilities.getFilmListArrayFromDb(urlString, getApplicationContext());
                        populateGridAdapter(movieList);
                    }
                }
            });



        } else showViews( true, "An error has occurred. Please try again" );
    }


    private void populateGridAdapter(List<MovieData> movieList) {
        if (movieList != null) {
            if (movieList.size() > 0) {
                // inicialização dos varios views a serem utilizados
                MovieGridAdapter mMovieGridAdapter = new MovieGridAdapter(MainActivity.this);
                mMovieGridAdapter.setMovieData(movieList);
                mRecyclerView.setAdapter(mMovieGridAdapter);
                mScrollView.scrollTo(0, 0);
                mProgressBar.setVisibility(View.INVISIBLE);
//                showhideButtons( jsonPages );
                showViews(false, null);
            } else showViews(true, "Your search returned no Movies");
        } else showViews(true, "An error has occurred. Please try again");

    }


}

