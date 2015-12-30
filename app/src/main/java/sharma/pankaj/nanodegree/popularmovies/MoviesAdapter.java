package sharma.pankaj.nanodegree.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.netio.NetIoUtills;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesDBHolder> {

    private final List<String> photoPaths;
    private final AppCompatActivity context;

    private MoviesAdapter(AppCompatActivity context, List<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.context = context;
    }

    @Override
    public MoviesDBHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_item, parent, false);
        return new MoviesDBHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesDBHolder holder, int position) {
        Picasso.with(context)
                .load(NetIoUtills.BASE_MOVIE_IMAGE_URL
                        + NetIoUtills.MOVIES_THUMBNAIL_SIZE + photoPaths.get(position))
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }
}
