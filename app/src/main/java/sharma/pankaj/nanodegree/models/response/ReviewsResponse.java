package sharma.pankaj.nanodegree.models.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import sharma.pankaj.nanodegree.models.Review;

/**
 * Created by Cyph3r on 04/02/16.
 */
public class ReviewsResponse {
    @SerializedName("id")
    int id;
    @SerializedName("page")
    int page;
    @SerializedName("total_pages")
    int totalPages;
    @SerializedName("total_results")
    int totalResults;
    @SerializedName("results")
    List<Review> reviews;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Review> getReviews() {
        return reviews;
    }
}
