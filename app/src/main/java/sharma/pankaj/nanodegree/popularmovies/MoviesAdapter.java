package sharma.pankaj.nanodegree.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;
import sharma.pankaj.nanodegree.netio.NetIoUtils;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesDBHolder> {

    private final List<MoviesDB> moviesDBList;
    private final AppCompatActivity context;

    public MoviesAdapter(AppCompatActivity context, List<MoviesDB> moviesDBList) {
        this.moviesDBList = moviesDBList;
        this.context = context;
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
                context.startActivity(new Intent(context, MovieDetailActivity.class)
                        .putExtra(MovieDetailActivity.ARG_OBJECT, moviesDBList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesDBList.size();
    }
}
