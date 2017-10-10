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

    public String getOriginalPosterPath() {
        return poster_path;
    }

    public String getOriginalBackdropPath() {
        return backdrop_path;
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

    public String getOriginalReleaseDate() {
        return release_date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
