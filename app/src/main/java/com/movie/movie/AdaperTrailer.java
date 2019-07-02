package com.movie.movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Msi-Locale on 16/02/2017.
 */

public class AdaperTrailer extends RecyclerView.Adapter<AdaperTrailer.ViewHolderTrailer> {
    Context cx;
    List<DataModelTrailer> listDataTrailer;
    private OnItemClick mClickListener;

    public void setmClickListener(OnItemClick mClickListener) {
        this.mClickListener = mClickListener;
    }

    public AdaperTrailer(Context cx, List<DataModelTrailer> listDataTrailer) {
        this.cx = cx;
        this.listDataTrailer = listDataTrailer;
    }

    @Override
    public ViewHolderTrailer onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout,parent,false);
        ViewHolderTrailer viewHolderTrailer = new ViewHolderTrailer(view);
        return viewHolderTrailer;
    }

    @Override
    public void onBindViewHolder(ViewHolderTrailer holder, int position) {
        DataModelTrailer dataModelTrailer = listDataTrailer.get(position);
        holder.mTitleTrailer.setText(dataModelTrailer.getmTitle());

    }

    @Override
    public int getItemCount() {
        return listDataTrailer.size();
    }

    public class ViewHolderTrailer extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitleTrailer;

        public ViewHolderTrailer(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTrailer = (TextView)itemView.findViewById(R.id.titleTrailer);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.OnItemClicked(view,getAdapterPosition());
            }
        }
    }
}
