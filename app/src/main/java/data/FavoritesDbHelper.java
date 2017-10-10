package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rodrigo.camara on 10-Oct-17.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteMovies.TABLE_NAME + " (" +
                FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_BACKDROP_PATH + " TEXT NOT NULL );";

        // COMPLETED (7) Execute the query by calling execSQL on sqLiteDatabase and pass the string query SQL_CREATE_WAITLIST_TABLE
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
