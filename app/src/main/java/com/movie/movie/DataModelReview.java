package com.movie.movie;

/**
 * Created by Msi-Locale on 16/02/2017.
 */

public class DataModelReview {
    String mAuthor;
    String mContent;

    public DataModelReview(String mAuthor, String mContent, String mUrl) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;

    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }


}
