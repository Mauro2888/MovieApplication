package com.movie.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.HolderData>{
    List<DataModelMovie> mDataListMovie;
    Context context;
    private OnItemClick clickListener = null;

    public AdapterMovie(List<DataModelMovie> mDataListMovie, Context context) {
        this.mDataListMovie = mDataListMovie;
        this.context = context;
    }

    public void setClickListener(OnItemClick clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        HolderData holderData = new HolderData(view);
        return holderData;
    }

    @Override
    public void onBindViewHolder(final HolderData holder, int position) {
        final DataModelMovie dataModelMovie = mDataListMovie.get(position);
        Glide.with(context).load(dataModelMovie.getImgMovie()).centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT).into(holder.mImageViewMovie);
        holder.mTitleMovie.setText(dataModelMovie.getmTitle());
        holder.mVoteMovie.setText(dataModelMovie.getmVote());
    }

    @Override
    public int getItemCount() {
        return mDataListMovie.size();
    }

    public class HolderData extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mImageViewMovie;
        TextView mTitleMovie;
        TextView mVoteMovie;
        public HolderData(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageViewMovie = (ImageView)itemView.findViewById(R.id.imageViewMovie);
            mTitleMovie = (TextView) itemView.findViewById(R.id.title_movie);
            mVoteMovie = (TextView) itemView.findViewById(R.id.vote_movie);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null){
                clickListener.OnItemClicked(view,getAdapterPosition());
            }
        }
    }
}

