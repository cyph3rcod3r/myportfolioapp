package sharma.pankaj.nanodegree.netio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class NetIoUtils {
    // URls
    public static final String BASE_MOVIE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/discover/movie";

    //params
    public static final String MOVIES_THUMBNAIL_SIZE = "w185/";
    public static final String SORT_POPULARITY = "popularity.desc";
    public static final String SORT_HIGHEST_RATED = "vote_count.desc";

    public static final String PARAM_SORT = "?sort_by=";
    public static final String PARAM_API_KEY = "&api_key=";


    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
