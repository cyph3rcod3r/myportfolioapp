package sharma.pankaj.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import sharma.pankaj.nanodegree.R;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class PopularMoviesActivity extends AppCompatActivity {

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

    }

}
