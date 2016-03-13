package sharma.pankaj.nanodegree.popularmovies;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.Review;

/**
 * Created by Cyph3r on 06/02/16.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {
    public ReviewsAdapter(Context context) {
        super(context, 0);
    }

    public static final String NO_REVIEW = "No Reviews Found";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_review, null);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.txvName.setText(getItem(position).getAuthor());
        holder.txvReview.setText(getItem(position).getContent());

        if (getItem(position).getAuthor().equalsIgnoreCase(NO_REVIEW)) {
            holder.imvIcon.setVisibility(View.GONE);
            holder.txvName.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            holder.imvIcon.setVisibility(View.VISIBLE);
            holder.txvName.setGravity(Gravity.LEFT);
        }

        return convertView;
    }

    @Override
    public Review getItem(int position) {
        return super.getItem(position);
    }

    static class Holder {
        TextView txvReview;
        TextView txvName;
        ImageView imvIcon;

        public Holder(View v) {
            txvName = (TextView) v.findViewById(R.id.txv_name);
            txvReview = (TextView) v.findViewById(R.id.txv_review);
            imvIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }
}
