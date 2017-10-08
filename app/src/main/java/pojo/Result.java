package pojo;

import org.parceler.Parcel;
import org.parceler.ParcelFactory;

import java.util.List;

/**
 * Created by rodrigo.camara on 25/08/2017.
 */
@Parcel
public class Result {

    String page;
    String total_pages;
    String total_results;
    List<Movie> results;

    @ParcelFactory
    public static Result create(List<Movie> results) {
        return new Result(results);
    }

    public List<Movie> getResults() {
        return results;
    }

    public Result(List<Movie> result) {
        this.results = result;
    }

}
