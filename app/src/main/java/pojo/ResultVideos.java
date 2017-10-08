package pojo;

import org.parceler.ParcelFactory;

import java.util.List;

/**
 * Created by Ro on 07/10/2017.
 */

public class ResultVideos {

    List<MovieVideos> results;

    public static ResultVideos create(List<MovieVideos> results) {
        return new ResultVideos(results);
    }

    public List<MovieVideos> getResults() {
        return results;
    }

    public ResultVideos(List<MovieVideos> result) {
        this.results = result;
    }
}
