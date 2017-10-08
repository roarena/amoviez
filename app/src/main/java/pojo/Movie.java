package pojo;

import android.net.Uri;

import org.parceler.Parcel;

import java.net.MalformedURLException;
import java.net.URL;

import utils.Constants;

/**
 * Created by rodrigo.camara on 25/08/2017.
 */
@Parcel
public class Movie {

    private String id;
    private String original_title;
    private String poster_path;
    private String overview;
    private float vote_average;
    private String release_date;
    private String backdrop_path;

    public String getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }


    public URL getPoster_path() {
        Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon().
                appendPath(Constants.QUERY_IMAGE_SIZE).
                appendPath(poster_path.substring(1))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public URL getBackdrop_path() {
        Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon().
                appendPath(Constants.QUERY_IMAGE_SIZE).
                appendPath(backdrop_path.substring(1))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public String getOverview() {
        return overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        //This will return the year ONLY.
        return release_date.substring(0, release_date.indexOf("-"));
    }
}
