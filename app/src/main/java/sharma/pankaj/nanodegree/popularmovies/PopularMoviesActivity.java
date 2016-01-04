package sharma.pankaj.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.netio.MoviesDbListAsync;
import sharma.pankaj.nanodegree.netio.MoviesListGetter;
import sharma.pankaj.nanodegree.netio.NetIoUtils;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class PopularMoviesActivity extends AppCompatActivity implements MoviesListGetter {

    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pop_movies);

        mRecyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // get Movie List

        runMovieGetter(NetIoUtils.SORT_POPULARITY);
    }

    @Override
    public void getMoviesList(List<MoviesDB> moviesDBList) {

        if (moviesDBList.isEmpty()) {
            Snackbar.make(mRecyclerView, "Unable To Connect", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runMovieGetter(NetIoUtils.SORT_POPULARITY);
                        }
                    })
                    .show();
            return;
        }

        mAdapter = new MoviesAdapter(this, moviesDBList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                runMovieGetter(NetIoUtils.SORT_POPULARITY);
                return true;
            case R.id.action_highest_rated:
                runMovieGetter(NetIoUtils.SORT_HIGHEST_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runMovieGetter(String sortOrder) {

        if (NetIoUtils.isConnectingToInternet(this)) {
            new MoviesDbListAsync(this, this).execute(sortOrder);
        } else {
            Snackbar.make(mRecyclerView, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            runMovieGetter(NetIoUtils.SORT_POPULARITY);
                        }
                    })
                    .show();
        }
    }
}
