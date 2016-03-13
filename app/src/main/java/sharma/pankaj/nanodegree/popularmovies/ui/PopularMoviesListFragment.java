package sharma.pankaj.nanodegree.popularmovies.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.netio.NetIoUtils;
import sharma.pankaj.nanodegree.popularmovies.MoviesAdapter;
import sharma.pankaj.nanodegree.popularmovies.PopularMoviesActivity;

/**
 * Created by Cyph3r on 18/02/16.
 */
public class PopularMoviesListFragment extends BaseFragment {

    private static final String SCROLL_STATE = "previous_state";
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;

    private PopularMoviesActivity mActivity;
    private View rootView;
    private int gridColumnCount = 2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (PopularMoviesActivity) activity;

        if (mActivity.getResources().getBoolean(R.bool.is_tablet)) {
            gridColumnCount = 3;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                mActivity.runMovieGetter(NetIoUtils.SORT_POPULARITY);
                return true;
            case R.id.action_highest_rated:
                mActivity.runMovieGetter(NetIoUtils.SORT_HIGHEST_RATED);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = View.inflate(getActivity(), R.layout.fragment_movie_list, null);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
            mAdapter = new MoviesAdapter(getActivity(), mActivity);
            mRecyclerView.setAdapter(mAdapter);
        }
        // get Movie List

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirstTimeLoad()) {
            setIsFirstTimeLoad(false);
        }
    }

    public void setMovieList(List<MoviesDB> moviesDBList) {
        mAdapter.clearMovies();
        mAdapter.setMovies(moviesDBList);
        mRecyclerView.getLayoutManager().scrollToPosition(mActivity.getCurrentItem());
    }
}
