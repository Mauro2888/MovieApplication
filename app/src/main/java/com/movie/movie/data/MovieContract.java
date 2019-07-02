package com.movie.movie.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Msi-Locale on 13/02/2017.
 */

public class MovieContract {
    public static final String AUTHORITY = "com.movie.movie";
    public static final String PATH_TABLE_MOVIE = "moviesItems";
    public static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);

    public MovieContract() {
    }

    public static class MovieEntry implements BaseColumns {

        public static final Uri URI_CONTENT = Uri.withAppendedPath(URI_BASE, PATH_TABLE_MOVIE);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/"
                + PATH_TABLE_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/"
                + PATH_TABLE_MOVIE;


        public static final String TABLE_NAME = "movies";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_RATE = "rate";
    }
}
