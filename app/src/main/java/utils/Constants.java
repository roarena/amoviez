package utils;

/**
 * Created by rodrigo.camara on 24/08/2017.
 */

public class Constants {
    // STRINGS
    public static final String MOVIEDB_BASE_URL = "https://api.themoviedb.org/";
    public static final String API_VERSION = "3";
    public static final String QUERY_TYPE = "movie";
    public static final String PARAM_API = "api_key";

    public static final String MOVIE_QUOTES_FILE = "movie-quotes";
    public static final String LINE_SEPARATOR = "line.separator";
    public static final String UTF_8 = "UTF-8";
    public static final String HTTP_ERROR = "ERROR";
    public static final String PARCEL_NAME = "QUERY_RESULTS";

    public static final String PARCEL_TOP_RATED = PARCEL_NAME + 1;
    public static final String PARCEL_POPULAR = PARCEL_NAME + 0;
    public static final String PARCEL_MOVIE = "MOVIE";


    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String QUERY_IMAGE_SIZE = "w500";


    public static final String REQUEST_POPULAR_SORT = "popular";
    public static final String REQUEST_TOP_RATED_SORT = "top_rated";

    // INTS
    public static final int TIME_REFRESH_QUOTE = 6000;
    public static final int CONNECTION_TIMEOU = 20000;
}
