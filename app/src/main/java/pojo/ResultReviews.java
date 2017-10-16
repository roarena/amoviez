package pojo;

import java.util.List;

/**
 * Created by Ro on 07/10/2017.
 */

public class ResultReviews {
    List<Review> results;

    public static ResultReviews create(List<Review> results) {
        return new ResultReviews(results);
    }

    public List<Review> getResults() {
        return results;
    }

    public ResultReviews(List<Review> result) {
        this.results = result;
    }
}
