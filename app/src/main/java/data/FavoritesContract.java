package data;

import android.provider.BaseColumns;

/**
 * Created by rodrigo.camara on 10-Oct-17.
 */

public class FavoritesContract {

    public static final class FavoriteMovies implements BaseColumns {
        public static final String TABLE_NAME = "favoriteMovies";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_ORIGINAL_TITLE = "originaltitle";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";

    }

}
