package sharma.pankaj.nanodegree.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.handler.SqliteHandler;
import sharma.pankaj.nanodegree.interfaces.OnItemClickListener;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.models.response.MoviesDbResponse;
import sharma.pankaj.nanodegree.netio.NetIoUtils;
import sharma.pankaj.nanodegree.netio.RestClient;
import sharma.pankaj.nanodegree.popularmovies.ui.MovieDetailFragment;
import sharma.pankaj.nanodegree.popularmovies.ui.PopularMoviesListFragment;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class PopularMoviesActivity extends AppCompatActivity implements OnItemClickListener<MoviesDB>, Callback<MoviesDbResponse> {

    private PopularMoviesListFragment popularMoviesListFragment;
    private MovieDetailFragment movieDetailFragment;
    private boolean isDualPane;
    private RestClient mRestClient;
    private LinearLayout mParentLayout;
    private ArrayList<MoviesDB> currentMovieList;
    private ProgressBar mProgressBar;
    private int currentItem = 0;
    private SqliteHandler sqliteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pop_movies);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mRestClient = new RestClient();
        sqliteHandler = new SqliteHandler(this);
        mParentLayout = (LinearLayout) findViewById(R.id.parent);
        mProgressBar = (ProgressBar) findViewById(R.id.pbr_activity_pop);
        View container = findViewById(R.id.pop_movies_container);
        isDualPane = container == null ? true : false;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("list")) {
                currentMovieList = savedInstanceState.getParcelableArrayList("list");
            }
            if (savedInstanceState.containsKey("item")) {
                currentItem = savedInstanceState.getInt("item");
            }
            popularMoviesListFragment = (PopularMoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.pop_movies_container);
        }
        if (!isDualPane) {
            if (popularMoviesListFragment == null) {
                popularMoviesListFragment = (PopularMoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.pop_movies_container);
            }
        } else {
            popularMoviesListFragment = (PopularMoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.list_fragment);
            movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
        }
        if (currentMovieList == null || currentMovieList.isEmpty()) {
            runMovieGetter(NetIoUtils.SORT_POPULARITY);
        } else {
            popularMoviesListFragment.setMovieList(currentMovieList);
            if (movieDetailFragment != null && currentMovieList.size() > currentItem) {
                movieDetailFragment.setData(currentMovieList.get(currentItem));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", currentMovieList);
        outState.putInt("item", currentItem);
    }

    public void runMovieGetter(String sortOrder) {
        if (NetIoUtils.isConnectingToInternet(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<MoviesDbResponse> call = mRestClient
                    .getMovieApis()
                    .getMoviesList(getString(R.string.movies_db_api), sortOrder);
            call.enqueue(this);

        } else {
            if (sqliteHandler.getTableRowCount(SqliteHandler.TABLE_FAVOURITE_MOVIES) > 0) {
                loadFavouriteMovies();
                return;
            }
            Snackbar.make(mParentLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runMovieGetter(NetIoUtils.SORT_POPULARITY);
                        }
                    })
                    .show();
        }
    }

    public void loadFavouriteMovies() {
        if (sqliteHandler.getTableRowCount(SqliteHandler.TABLE_FAVOURITE_MOVIES) <= 0) {
            Snackbar.make(mParentLayout, "You don't have any favourite movies.", Snackbar.LENGTH_LONG)
                    .show();
            return;
        }
        List<MoviesDB> favouriteList = sqliteHandler.getAllFavouriteMovies();
        currentMovieList = new ArrayList<>(favouriteList);
        popularMoviesListFragment.setMovieList(currentMovieList);

        if (isDualPane && movieDetailFragment != null) {
            movieDetailFragment.setData(currentMovieList.get(0));
        }
    }


    public void openMoviesDetails(MoviesDB moviesDB) {
        currentItem = currentMovieList.indexOf(moviesDB);
        if (isDualPane && movieDetailFragment != null) {
            movieDetailFragment.setData(moviesDB);
        } else {
            startActivity(new Intent(this, MovieDetailActivity.class)
                    .putExtra(MovieDetailActivity.ARG_OBJECT, moviesDB));
        }
    }

    @Override
    public void onItemClick(MoviesDB object) {
        openMoviesDetails(object);
    }

    @Override
    public void onResponse(Response<MoviesDbResponse> response) {
        mProgressBar.setVisibility(View.GONE);
        currentMovieList = new ArrayList<>(response.body().getMoviesDBList());
        popularMoviesListFragment.setMovieList(currentMovieList);

        if (isDualPane && movieDetailFragment != null && !currentMovieList.isEmpty()) {
            movieDetailFragment.setData(currentMovieList.get(0));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        mProgressBar.setVisibility(View.GONE);
        Log.e("Error -", "" + t.getMessage());
        Snackbar.make(mParentLayout, "Unable To Connect.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runMovieGetter(NetIoUtils.SORT_POPULARITY);
                    }
                })
                .show();
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
}
