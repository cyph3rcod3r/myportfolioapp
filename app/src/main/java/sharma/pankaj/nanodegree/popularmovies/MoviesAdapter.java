package sharma.pankaj.nanodegree.popularmovies;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.interfaces.OnItemClickListener;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.netio.NetIoUtils;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesDBHolder> {

    private List<MoviesDB> moviesDBList = new ArrayList<>();
    private final FragmentActivity context;
    private final OnItemClickListener listener;

    public MoviesAdapter(FragmentActivity context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public MoviesDBHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, parent, false);
        return new MoviesDBHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesDBHolder holder, final int position) {
        Picasso.with(context)
                .load(NetIoUtils.BASE_MOVIE_IMAGE_URL
                        + NetIoUtils.MOVIES_THUMBNAIL_SIZE + moviesDBList.get(position).getPosterPath())
                .into(holder.mImageView);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(moviesDBList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesDBList.size();
    }

    /**
     * Set New Items, Make sure to clear first
     *
     * @param moviesDBList
     */
    public void setMovies(List<MoviesDB> moviesDBList) {
        this.moviesDBList.clear();
        this.moviesDBList.addAll(moviesDBList);
        this.notifyItemRangeInserted(0, this.moviesDBList.size() - 1);
    }

    public void clearMovies() {
        int size = this.moviesDBList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.moviesDBList.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

}
