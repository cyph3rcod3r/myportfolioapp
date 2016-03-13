package sharma.pankaj.nanodegree.popularmovies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Cyph3r on 05/03/16.
 */
public class BaseFragment extends Fragment {
    private boolean isFirstTimeLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean isFirstTimeLoad() {
        return isFirstTimeLoad;
    }

    public void setIsFirstTimeLoad(boolean isFirstTimeLoad) {
        this.isFirstTimeLoad = isFirstTimeLoad;
    }
}
