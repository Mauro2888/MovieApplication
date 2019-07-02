package com.movie.movie;

import java.io.Serializable;


public class DataModelMovie implements Serializable {
    String imgMovie;
    String mTitle;
    String mPlot;
    String mDate;
    String mVote;
    String mTrailer;
    int mPos;

    public DataModelMovie(String imgMovie, String mTitle, String mPlot, String mDate, String mVote, String mTrailer, int mPos) {
        this.imgMovie = imgMovie;
        this.mTitle = mTitle;
        this.mPlot = mPlot;
        this.mDate = mDate;
        this.mVote = mVote;
        this.mTrailer = mTrailer;
        this.mPos = mPos;
    }

    public String getImgMovie() {
        return imgMovie;
    }

    public void setImgMovie(String imgMovie) {
        this.imgMovie = imgMovie;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmPlot() {
        return mPlot;
    }

    public void setmPlot(String mPlot) {
        this.mPlot = mPlot;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmVote() {
        return mVote;
    }

    public void setmVote(String mVote) {
        this.mVote = mVote;
    }

    public String getmTrailer() {
        return mTrailer;
    }

    public void setmTrailer(String mTrailer) {
        this.mTrailer = mTrailer;
    }

    public int getmPos() {
        return mPos;
    }

    public void setmPos(int mPos) {
        this.mPos = mPos;
    }
}





