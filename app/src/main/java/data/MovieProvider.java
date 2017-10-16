package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static data.FavoritesContract.FavoriteMovies.TABLE_NAME;

/**
 * Created by rodrigo.camara on 16-Oct-17.
 */

public class MovieProvider extends ContentProvider {

    private FavoritesDbHelper favoritesDbHelper;
    public static final int MOVIES = 666;
    public static final int MOVIES_ID = 667;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.FAVORITE_MOVIES_PATH, MOVIES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.FAVORITE_MOVIES_PATH + "/*", MOVIES_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        favoritesDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = favoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;
        switch (match) {
            case MOVIES:
                returnCursor = db.query(
                        FavoritesContract.FavoriteMovies.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        FavoritesContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoriteMovies.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = favoritesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int moviesDeleted;

        switch (match) {
            case MOVIES_ID:
                String id = uri.getPathSegments().get(1);
                moviesDeleted = db.delete(TABLE_NAME, "movieID=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
