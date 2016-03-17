package sharma.pankaj.nanodegree.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sharma.pankaj.nanodegree.models.MoviesDB;

public class SqliteHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "portfolio";

    // Contacts table name
    public static final String TABLE_FAVOURITE_MOVIES = "favourite_movies";

    // Contacts Table Columns names
    private static final String KEY_ID = "movieId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POSTER = "poster_path";
    private static final String KEY_SYNOPSIS = "plot";
    private static final String KEY_USER_RATING = "user_rating";
    private static final String KEY_RELEASE_DATE = "release_date";

    public SqliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MOVIES_TABLE = "CREATE TABLE " + TABLE_FAVOURITE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_POSTER + " TEXT,"
                + KEY_RELEASE_DATE + " TEXT,"
                + KEY_SYNOPSIS + " TEXT,"
                + KEY_USER_RATING + " DOUBLE,"
                + KEY_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_MOVIES);

        onCreate(db);
    }

    public void addFavouriteMovie(MoviesDB moviesDB) {

        if (getFavouriteMovie(moviesDB.getId()) != null) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, moviesDB.getId());
        values.put(KEY_POSTER, moviesDB.getPosterPath());
        values.put(KEY_RELEASE_DATE, moviesDB.getReleaseDate());
        values.put(KEY_SYNOPSIS, moviesDB.getOverview());
        values.put(KEY_USER_RATING, moviesDB.getVoteAverage());
        values.put(KEY_TITLE, moviesDB.getTitle());

        db.insert(TABLE_FAVOURITE_MOVIES, null, values);
        db.close();
    }

    public MoviesDB getFavouriteMovie(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVOURITE_MOVIES, new String[]{KEY_ID,
                        KEY_POSTER, KEY_RELEASE_DATE, KEY_SYNOPSIS, KEY_USER_RATING, KEY_TITLE}, KEY_ID + "=?",
                new String[]{String.valueOf(movieId)}, null, null, null, null);


        if (cursor != null && cursor.moveToFirst()) {
            MoviesDB moviesDB = new MoviesDB();
            moviesDB.setId(Integer.parseInt(cursor.getString(0)));
            moviesDB.setPosterPath(cursor.getString(1));
            moviesDB.setReleaseDate(cursor.getString(2));
            moviesDB.setOverview(cursor.getString(3));
            moviesDB.setVoteAverage(cursor.getDouble(4));
            moviesDB.setTitle(cursor.getString(5));
            return moviesDB;
        }
        return null;
    }

    public List<MoviesDB> getAllFavouriteMovies() {
        List<MoviesDB> favouriteList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MoviesDB moviesDB = new MoviesDB();
                moviesDB.setId(Integer.parseInt(cursor.getString(0)));
                moviesDB.setPosterPath(cursor.getString(1));
                moviesDB.setReleaseDate(cursor.getString(2));
                moviesDB.setOverview(cursor.getString(3));
                moviesDB.setVoteAverage(cursor.getDouble(4));
                moviesDB.setTitle(cursor.getString(5));
                favouriteList.add(moviesDB);
            } while (cursor.moveToNext());
        }

        return favouriteList;
    }

    public int updateFavouriteMovie(MoviesDB moviesDB) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, moviesDB.getId());
        values.put(KEY_POSTER, moviesDB.getPosterPath());
        values.put(KEY_RELEASE_DATE, moviesDB.getReleaseDate());
        values.put(KEY_SYNOPSIS, moviesDB.getOverview());
        values.put(KEY_USER_RATING, moviesDB.getVoteAverage());
        values.put(KEY_TITLE, moviesDB.getTitle());

        return db.update(TABLE_FAVOURITE_MOVIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(moviesDB.getId())});
    }

    public int deleteFavouriteMovie(MoviesDB moviesDB) {

        if (getFavouriteMovie(moviesDB.getId()) == null) {
            return 0;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        int affect = db.delete(TABLE_FAVOURITE_MOVIES, KEY_ID + " = ?",
                new String[]{String.valueOf(moviesDB.getId())});
        Log.e("SQL delete", String.valueOf(affect));
        db.close();
        return affect;
    }


    public int getTableRowCount(String tableName) {
        int count;
        String countQuery = "SELECT  * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }

}