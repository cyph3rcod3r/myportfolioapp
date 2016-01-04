package sharma.pankaj.nanodegree.popularmovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.netio.NetIoUtils;

/**
 * Created by Cyph3r on 04/01/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private MoviesDB moviesDB;
    public static final String ARG_OBJECT = MoviesDB.class.getName();
    private TextView txvTitle, txvYear, txvRunningTime, txvRate, txvDesc;
    private ImageView imvThumb;
    private LinearLayout movieContainer;
    private SimpleDateFormat mYearFormat = new SimpleDateFormat("yyyy");
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.pop_movies_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            if (getIntent().hasExtra(ARG_OBJECT)) {
                moviesDB = getIntent().getParcelableExtra(ARG_OBJECT);
            }
        }

        txvDesc = (TextView) findViewById(R.id.txv_movie_detail_desc);
        txvRate = (TextView) findViewById(R.id.txv_movie_detail_rating);
        txvRunningTime = (TextView) findViewById(R.id.txv_movie_detail_running_time);
        txvTitle = (TextView) findViewById(R.id.txv_movie_detail_title);
        txvYear = (TextView) findViewById(R.id.txv_movie_detail_year);

        imvThumb = (ImageView) findViewById(R.id.imv_movie_detail_thumb);
        movieContainer = (LinearLayout) findViewById(R.id.movie_container);

        setData(moviesDB);
    }

    private void setData(MoviesDB object) {
        if (object == null) {
            showInfo();
            return;
        }

        try {
            txvYear.setText(mYearFormat.format(mDateFormat.parse(object.getReleaseDate())));
            txvTitle.setText(object.getTitle());
            txvRate.setText(object.getVoteAverage() + "/" + "10");
            txvDesc.setText(object.getOverview());

            Picasso.with(this).load(NetIoUtils.BASE_MOVIE_IMAGE_URL
                    + NetIoUtils.MOVIES_THUMBNAIL_SIZE + object.getPosterPath()).into(imvThumb);
        } catch (ParseException e) {
            e.printStackTrace();
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

    private void showInfo() {
        Snackbar.make(movieContainer, "Data No Found", Snackbar.LENGTH_INDEFINITE).setAction("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).show();
    }
}
