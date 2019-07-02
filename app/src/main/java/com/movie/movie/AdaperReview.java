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

public class AdaperReview extends RecyclerView.Adapter<AdaperReview.ViewHolderReview> {
    Context cx;
    List<DataModelReview> listDataReview;

    public AdaperReview(Context cx, List<DataModelReview> listDataReview) {
        this.cx = cx;
        this.listDataReview = listDataReview;
    }

    @Override
    public ViewHolderReview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout,parent,false);
        ViewHolderReview viewHolderReview = new ViewHolderReview(view);
        return viewHolderReview;
    }

    @Override
    public void onBindViewHolder(ViewHolderReview holder, int position) {
        DataModelReview dataModelReview = listDataReview.get(position);
        holder.mAuthor.setText(dataModelReview.getmAuthor());
        holder.mContent.setText(dataModelReview.getmContent());

    }

    @Override
    public int getItemCount() {
        return listDataReview.size();
    }

    public class ViewHolderReview extends RecyclerView.ViewHolder {
        TextView mAuthor;
        TextView mContent;

        public ViewHolderReview(View itemView) {
            super(itemView);
            mAuthor = (TextView)itemView.findViewById(R.id.authorReview);
            mContent = (TextView)itemView.findViewById(R.id.reviewTxt);
        }
    }
}
