package sharma.pankaj.nanodegree.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesDBHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;

    public MoviesDBHolder(ImageView itemView) {
        super(itemView);
        this.mImageView = itemView;
    }

}
