package sharma.pankaj.nanodegree.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import sharma.pankaj.nanodegree.models.Trailer;

/**
 * Created by Cyph3r on 04/02/16.
 */
public class TrailersResponse {

    @SerializedName("id")
    int id;
    @SerializedName("results")
    List<Trailer> trailers;

    public int getId() {
        return id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }
}
