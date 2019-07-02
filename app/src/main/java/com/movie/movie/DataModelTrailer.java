package com.movie.movie;

/**
 * Created by Msi-Locale on 16/02/2017.
 */

public class DataModelTrailer {

    String mTitle;
    String mTrailerUri;


    public DataModelTrailer(String mTitle, String mTrailerUri) {
        this.mTitle = mTitle;
        this.mTrailerUri = mTrailerUri;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmTrailerUri() {
        return mTrailerUri;
    }

    public void setmTrailerUri(String mTrailerUri) {
        this.mTrailerUri = mTrailerUri;
    }

    @Override
    public String toString() {
        return "DataModelTrailer{" +
                "mTitle='" + mTitle + '\'' +
                ", mTrailerUri='" + mTrailerUri + '\'' +
                '}';
    }
}
