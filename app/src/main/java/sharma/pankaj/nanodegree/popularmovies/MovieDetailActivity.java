package sharma.pankaj.nanodegree.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.models.Review;
import sharma.pankaj.nanodegree.models.Trailer;
import sharma.pankaj.nanodegree.models.response.ReviewsResponse;
import sharma.pankaj.nanodegree.models.response.TrailersResponse;
import sharma.pankaj.nanodegree.netio.NetIoUtils;
import sharma.pankaj.nanodegree.netio.RestClient;

/**
 * Created by Cyph3r on 04/01/16.
 */
public class MovieDetailActivity extends AppCompatActivity implements Callback<TrailersResponse> {

    private MoviesDB moviesDB;
    public static final String ARG_OBJECT = MoviesDB.class.getName();
    private TextView txvTitle, txvYear, txvRunningTime, txvRate, txvDesc;
    private ImageView imvThumb;
    private LinearLayout movieContainer;
    private SimpleDateFormat mYearFormat = new SimpleDateFormat("yyyy");
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private View headerView;
    private ListView listView;
    private GridLayout gridLayout;
    private RestClient mRestClient;
    private ProgressBar pbTrailer;
    private ProgressBar pbReviews;
    private ReviewsAdapter mReviewsAdapter;

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
            }
        }
        mRestClient = new RestClient();
        listView = (ListView) findViewById(R.id.listview_reviews);
        mReviewsAdapter = new ReviewsAdapter(this);
        initListViewHeader();

    }

    private void initListViewHeader() {
        if (headerView == null) {
            headerView = View.inflate(this, R.layout.header_movie_detail, null);
        }
        txvDesc = (TextView) headerView.findViewById(R.id.txv_movie_detail_desc);
        txvRate = (TextView) headerView.findViewById(R.id.txv_movie_detail_rating);
        txvRunningTime = (TextView) headerView.findViewById(R.id.txv_movie_detail_running_time);
        txvTitle = (TextView) headerView.findViewById(R.id.txv_movie_detail_title);
        txvYear = (TextView) headerView.findViewById(R.id.txv_movie_detail_year);
        pbTrailer = (ProgressBar) headerView.findViewById(R.id.pb_trailers);
        pbReviews = (ProgressBar) headerView.findViewById(R.id.pb_reviews);

        imvThumb = (ImageView) headerView.findViewById(R.id.imv_movie_detail_thumb);
        movieContainer = (LinearLayout) headerView.findViewById(R.id.movie_container);
        setData(moviesDB);
        gridLayout = (GridLayout) headerView.findViewById(R.id.grid_layout_trailers);

        listView.addHeaderView(headerView, null, false);
        listView.setAdapter(mReviewsAdapter);
        getTrailers();
        getReviews();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void showInfo() {
        Snackbar.make(movieContainer, "Data Not Found", Snackbar.LENGTH_INDEFINITE).setAction("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).show();
    }

    private void getTrailers() {
        pbTrailer.setVisibility(View.VISIBLE);
        if (NetIoUtils.isConnectingToInternet(this)) {
            Call<TrailersResponse> call = mRestClient
                    .getMovieApis()
                    .getMovieTrailers(moviesDB.getId(), getString(R.string.movies_db_api));
            call.enqueue(this);

        } else {
            Snackbar.make(listView, "Unable to load trailers at the moment..", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTrailers();
                        }
                    })
                    .show();
        }
    }

    private void getReviews() {
        pbReviews.setVisibility(View.VISIBLE);
        if (NetIoUtils.isConnectingToInternet(this)) {
            Call<ReviewsResponse> call = mRestClient
                    .getMovieApis()
                    .getMovieReviews(moviesDB.getId(), getString(R.string.movies_db_api));
            call.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(Response<ReviewsResponse> response) {
                    pbReviews.setVisibility(View.GONE);
                    if (response.body() != null && !response.body().getReviews().isEmpty()) {
                        mReviewsAdapter.addAll(response.body().getReviews());
                    } else {
                        mReviewsAdapter.add(new Review(ReviewsAdapter.NO_REVIEW, ""));
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    pbReviews.setVisibility(View.GONE);
                }
            });

        } else {
            Snackbar.make(listView, "Unable to load reviews at the moment..", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTrailers();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onResponse(Response<TrailersResponse> response) {
        loadTrailer(response.body().getTrailers());
    }

    @Override
    public void onFailure(Throwable t) {

    }

    private void loadTrailer(List<Trailer> trailers) {
        if (trailers == null || trailers.isEmpty()) {
            return;
        }
        for (Trailer t : trailers) {
            gridLayout.addView(prepareLayoutForTrailer(t));
        }
    }

    private View prepareLayoutForTrailer(final Trailer t) {
        final RelativeLayout layout = new RelativeLayout(this);
        final ImageView view = new ImageView(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetIoUtils.YOUTUBE_URL + t.getKey())));
            }
        });
        Picasso.with(this)
                .load(NetIoUtils.YOUTUBE_THUMBNAIL_URL
                        + t.getKey() + NetIoUtils.YOUTUBE_THUMBNAIL_FILE)
                .into(view, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ImageView icon = new ImageView(MovieDetailActivity.this);
                        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        icon.setImageResource(R.drawable.ic_youtube);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        icon.setLayoutParams(layoutParams);
                        layout.addView(view);
                        layout.addView(icon);
                        pbTrailer.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        return layout;
    }


}
