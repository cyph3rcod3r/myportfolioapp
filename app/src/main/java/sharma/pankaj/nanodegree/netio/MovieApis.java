package sharma.pankaj.nanodegree.netio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import sharma.pankaj.nanodegree.models.response.MoviesDbResponse;
import sharma.pankaj.nanodegree.models.response.ReviewsResponse;
import sharma.pankaj.nanodegree.models.response.TrailersResponse;

/**
 * Created by Cyph3r on 04/02/16.
 */
public interface MovieApis {
    @GET("discover/movie")
    Call<MoviesDbResponse> getMoviesList(@Query("api_key") String apiKey, @Query("sort_by") String sortOrder);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);

}
