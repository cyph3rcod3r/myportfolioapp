package sharma.pankaj.nanodegree.netio;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Cyph3r on 04/02/16.
 */
public class RestClient {

    private MovieApis movieApis;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetIoUtils.BASE_MOVIE_DB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        movieApis = retrofit.create(MovieApis.class);
    }

    public MovieApis getMovieApis() {
        return movieApis;
    }

}
