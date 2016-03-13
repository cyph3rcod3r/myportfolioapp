package sharma.pankaj.nanodegree.popularmovies.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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
import sharma.pankaj.nanodegree.popularmovies.PopularMoviesActivity;
import sharma.pankaj.nanodegree.popularmovies.ReviewsAdapter;

/**
 * Created by Cyph3r on 21/02/16.
 */
public class MovieDetailFragment extends BaseFragment implements Callback<TrailersResponse> {


    private static final String SCROLL_STATE = "previous_state";
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
    private PopularMoviesActivity mActivity;
    private View rootView;

    public static MovieDetailFragment newInstance() {
//        Bundle args = new Bundle();
        MovieDetailFragment fragment = new MovieDetailFragment();
//        args.putParcelable(ARG_OBJECT, moviesDB);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (PopularMoviesActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        moviesDB = getArguments().getParcelable(ARG_OBJECT);
        mRestClient = new RestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = View.inflate(mActivity, R.layout.fragment_movie_detail, null);
        listView = (ListView) rootView.findViewById(R.id.listview_reviews);
        mReviewsAdapter = new ReviewsAdapter(mActivity);
        initListViewHeader();
        return rootView;
    }

    private void initListViewHeader() {
        if (headerView == null) {
            headerView = View.inflate(mActivity, R.layout.header_movie_detail, null);
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
        gridLayout = (GridLayout) headerView.findViewById(R.id.grid_layout_trailers);
        listView.addHeaderView(headerView);
    }

    public void setData(MoviesDB object) {
        if (object == null) {
            showInfo();
            return;
        }
        try {
            txvYear.setText(mYearFormat.format(mDateFormat.parse(object.getReleaseDate())));
            txvTitle.setText(object.getTitle());
            txvRate.setText(object.getVoteAverage() + "/" + "10");
            txvDesc.setText(object.getOverview());
            Picasso.with(mActivity).load(NetIoUtils.BASE_MOVIE_IMAGE_URL
                    + NetIoUtils.MOVIES_THUMBNAIL_SIZE + object.getPosterPath()).into(imvThumb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        moviesDB = object;
        mReviewsAdapter = new ReviewsAdapter(mActivity);
        listView.setAdapter(mReviewsAdapter);
        gridLayout.removeAllViews();
        getTrailers();
        getReviews();
        setIsFirstTimeLoad(false);

    }

    private void showInfo() {
        Snackbar.make(movieContainer, "Data No Found", Snackbar.LENGTH_INDEFINITE).setAction("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onBackPressed();
            }
        }).show();
    }

    private void getTrailers() {
        pbTrailer.setVisibility(View.VISIBLE);
        if (NetIoUtils.isConnectingToInternet(mActivity)) {
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
        if (NetIoUtils.isConnectingToInternet(mActivity)) {
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
            View child = prepareLayoutForTrailer(t);
            gridLayout.addView(child);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams) child.getLayoutParams();
            params.width = (gridLayout.getWidth() / gridLayout.getColumnCount()) - params.rightMargin - params.leftMargin;
            child.setLayoutParams(params);
        }
    }

    private View prepareLayoutForTrailer(final Trailer t) {
        final RelativeLayout layout = new RelativeLayout(mActivity);
        final ImageView view = new ImageView(mActivity);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(NetIoUtils.YOUTUBE_URL + t.getKey())));
            }
        });
        view.setPadding(10, 0, 10, 0);
        Picasso.with(mActivity)
                .load(NetIoUtils.YOUTUBE_THUMBNAIL_URL
                        + t.getKey() + NetIoUtils.YOUTUBE_THUMBNAIL_FILE)
                .into(view, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ImageView icon = new ImageView(mActivity);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_STATE, listView.getFirstVisiblePosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            listView.smoothScrollToPosition(savedInstanceState.getInt(SCROLL_STATE));
            mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mActivity.invalidateOptionsMenu();
        }
    }
}
