package sharma.pankaj.nanodegree.netio;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sharma.pankaj.nanodegree.R;
import sharma.pankaj.nanodegree.models.MoviesDB;

/**
 * Created by Cyph3r on 29/12/15.
 */
public class MoviesDbListAsync extends AsyncTask<String, Void, List<MoviesDB>> {

    private static final int TIMEOUT = 10000;
    private final MoviesListGetter moviesListGetter;
    private AppCompatActivity mActivity;
    private String newValue = "0";
    private ProgressDialog progressDialog;

    public MoviesDbListAsync(AppCompatActivity mActivity, MoviesListGetter moviesListGetter) {
        this.moviesListGetter = moviesListGetter;
        this.mActivity = mActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mActivity, "", "Loading ...");
    }

    @Override
    protected List<MoviesDB> doInBackground(String... params) {
        return parseResponse(getResponse(NetIoUtils.BASE_MOVIE_DB_URL
                        + NetIoUtils.PARAM_SORT
                        + params[0]
                        + NetIoUtils.PARAM_API_KEY
                        + mActivity.getString(R.string.movies_db_api_test)
        ));
    }

    @Override
    protected void onPostExecute(List<MoviesDB> moviesDBs) {
        super.onPostExecute(moviesDBs);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        moviesListGetter.getMoviesList(moviesDBs);
    }

    public String getResponse(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(mActivity.getString(R.string.method_type));
            c.setRequestProperty(mActivity.getString(R.string.user_property), newValue);
            c.setUseCaches(false);
            c.setConnectTimeout(TIMEOUT);
            c.setReadTimeout(TIMEOUT);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case HttpURLConnection.HTTP_CREATED:
                case HttpURLConnection.HTTP_OK:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private List<MoviesDB> parseResponse(String response) {
        List<MoviesDB> popularMovies = new ArrayList<>();

        if (TextUtils.isEmpty(response)) {
            return popularMovies;
        }

        Gson gson = new Gson();
        try {
            JSONObject parent = new JSONObject(response);
            JSONArray movies = parent.getJSONArray("results");
            for (int i = 0; i < movies.length(); i++) {
                popularMovies.add(gson.fromJson(movies.getJSONObject(i).toString(), MoviesDB.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return popularMovies;
    }
}
