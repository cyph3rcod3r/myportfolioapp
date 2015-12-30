package sharma.pankaj.nanodegree.netio;

import android.os.AsyncTask;

import java.util.List;

import sharma.pankaj.nanodegree.models.MoviesDB;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesDbListAsync extends AsyncTask<String,Void,List<MoviesDB>> {

    private final MoviesListGetter moviesListGetter;

    public MoviesDbListAsync(MoviesListGetter moviesListGetter){
        this.moviesListGetter = moviesListGetter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<MoviesDB> doInBackground(String... params) {
        return null;
    }

    @Override
    protected void onPostExecute(List<MoviesDB> moviesDBs) {
        super.onPostExecute(moviesDBs);
    }
}
