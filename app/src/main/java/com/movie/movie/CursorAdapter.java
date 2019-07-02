package com.movie.movie;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.movie.movie.data.MovieContract;

/**
 * Created by Msi-Locale on 23/02/2017.
 */

public class CursorAdapter extends RecyclerView.Adapter<CursorAdapter.ViewHolderFavourite> {

    private Cursor mCursor;
    private Context mContext;
    public CursorAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public CursorAdapter.ViewHolderFavourite onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_layout, parent, false);
        CursorAdapter.ViewHolderFavourite viewHolderFavourite = new ViewHolderFavourite(view);
        return viewHolderFavourite;
    }

    @Override
    public void onBindViewHolder(CursorAdapter.ViewHolderFavourite holder, int position) {

        int id = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID);
        int nome = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_TITLE);
        int poster = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_POSTER);
        int date = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_DATE);
        int rate = mCursor.getColumnIndexOrThrow(MovieContract.MovieEntry.COLUMN_RATE);
        mCursor.moveToPosition(position);
        String titlemovie = mCursor.getString(nome);
        String posterMovie = mCursor.getString(poster);
        String dateMovie = mCursor.getString(date);
        String rateMovie = mCursor.getString(rate);
        int PosID = mCursor.getInt(id);
        holder.itemView.setTag(PosID);
        holder.mTitleFavourtite.setText(titlemovie);
        holder.mDateFavourtite.setText(dateMovie);
        holder.mRateFavourtite.setText(rateMovie);
        Glide.with(mContext).load(posterMovie).centerCrop().into(holder.mTitleImage);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor tempCursor = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return tempCursor;
    }

    public class ViewHolderFavourite extends RecyclerView.ViewHolder {

        TextView mRateFavourtite;
        TextView mDateFavourtite;
        TextView mTitleFavourtite;
        ImageView mTitleImage;

        public ViewHolderFavourite(View itemView) {
            super(itemView);

            mTitleFavourtite = (TextView) itemView.findViewById(R.id.TitleFavouriteMovie);
            mRateFavourtite = (TextView) itemView.findViewById(R.id.RateFavouriteMovie);
            mDateFavourtite = (TextView) itemView.findViewById(R.id.DateFavouriteMovie);
            mTitleImage = (ImageView) itemView.findViewById(R.id.imageFavouriteMovie);
        }

    }
}
