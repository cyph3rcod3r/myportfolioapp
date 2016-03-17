package sharma.pankaj.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.popularmovies.ui.MovieDetailFragment;

/**
 * Created by Cyph3r on 04/01/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private MoviesDB moviesDB;
    public static final String ARG_OBJECT = MoviesDB.class.getName();

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pop_movies_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        initToolbar();
        if (getIntent() != null) {
            if (getIntent().hasExtra(ARG_OBJECT)) {
                moviesDB = getIntent().getParcelableExtra(ARG_OBJECT);
                MovieDetailFragment movieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment);
                movieDetailFragment.setData(moviesDB);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
