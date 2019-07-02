package com.movie.movie;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.Toast;

import com.movie.movie.data.MovieContract;
import com.movie.movie.data.MovieHelper;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static int LOADER_ID = 22;
    public MovieHelper mMovieHelper;
    public SQLiteDatabase mSQLiteDatabase;
    private RecyclerView mRecyclerFav;
    private RecyclerView.LayoutManager mLayoutManFav;
    private CursorAdapter mAdapterCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        setTitle(R.string.favourtie);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRecyclerFav = (RecyclerView)findViewById(R.id.recyclerFavourtite);
        mLayoutManFav = new LinearLayoutManager(this);
        mRecyclerFav.setLayoutManager(mLayoutManFav);
        mRecyclerFav.setHasFixedSize(true);
        mRecyclerFav.setItemAnimator(new DefaultItemAnimator());

        mAdapterCursor = new CursorAdapter(this);
        mRecyclerFav.setAdapter(mAdapterCursor);
        mMovieHelper = new MovieHelper(this);
        Toast.makeText(this, "Swipe left to delete Favourite", Toast.LENGTH_SHORT).show();

        mSQLiteDatabase = mMovieHelper.getReadableDatabase();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int IdPos = (int) viewHolder.itemView.getTag();
                Uri uri = MovieContract.MovieEntry.URI_CONTENT.buildUpon().appendPath(String.valueOf(IdPos)).build();
                getContentResolver().delete(uri, null, null);
            }
        }).attachToRecyclerView(mRecyclerFav);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri UriMovie = MovieContract.MovieEntry.URI_CONTENT;
        CursorLoader cursorLoader = new CursorLoader(this, UriMovie, null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapterCursor.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapterCursor.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
