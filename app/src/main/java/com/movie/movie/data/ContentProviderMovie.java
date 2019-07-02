package com.movie.movie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


public class ContentProviderMovie extends ContentProvider {

    public static final int ALL_ROW = 100;
    public static final int SELECTED_ROW = 101;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TABLE_MOVIE, ALL_ROW);
        sUriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_TABLE_MOVIE + "/#", SELECTED_ROW);
    }

    private MovieHelper mMovieHelper;
    private SQLiteDatabase mSQLiteDatabase;

    @Override
    public boolean onCreate() {
        mMovieHelper = new MovieHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        Uri returnURI;
        mSQLiteDatabase = mMovieHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id;
        switch (match) {
            case ALL_ROW:
                id = mSQLiteDatabase.insertWithOnConflict(MovieContract.MovieEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnURI = ContentUris.withAppendedId(MovieContract.MovieEntry.URI_CONTENT, id);
                } else {
                    throw new IllegalArgumentException("Error Insert " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Error Insert " + uri);
        }
        getContext().getContentResolver().notifyChange(returnURI, null);
        System.out.println(returnURI.toString());
        return returnURI;

    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projector, String selection, String[] selectionArgs, String sortOrder) {
        mSQLiteDatabase = mMovieHelper.getReadableDatabase();
        Cursor mCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL_ROW:
                mCursor = mSQLiteDatabase.query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projector,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Error query " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL_ROW:
                return MovieContract.MovieEntry.CONTENT_LIST_TYPE;
            case SELECTED_ROW:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Error " + uri + match);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mSQLiteDatabase = mMovieHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int row;
        switch (match) {
            case SELECTED_ROW:
                String id = uri.getPathSegments().get(1);
                String mSelection = MovieContract.MovieEntry._ID + "= ?";
                String[] mSelectionArgs = new String[]{id};
                row = mSQLiteDatabase.delete(MovieContract.MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error delete " + uri);

        }
        if (row != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        System.out.println("RIGA" + row);
        return row;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        int match = sUriMatcher.match(uri);
        int count = 0;
        switch (match) {
            case ALL_ROW:
                count = mSQLiteDatabase.update(MovieContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            case SELECTED_ROW:
                selection = MovieContract.MovieEntry._ID + "= ?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                count = mSQLiteDatabase.update(MovieContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Error update " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
