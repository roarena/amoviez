package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by rodrigo.camara on 10-Oct-17.
 */

public class DatabaseUtils {
    //Database
    private static SQLiteDatabase mDb;

    public static SQLiteDatabase getDB(Context context){
        FavoritesDbHelper favoritesDbHelper = new FavoritesDbHelper(context);
        mDb = favoritesDbHelper.getWritableDatabase();
        return mDb;
    }
}
