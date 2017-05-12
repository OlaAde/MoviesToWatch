package com.example.adeogo.moviestowatch.models;

/**
 * Created by Adeogo on 4/30/2017.
 */

public class Review {
    private String mAuthorName;
    private String mReviewText;
    private String mReviewLink;

    public Review(String AuthorName, String ReviewText, String ReviewLink){
        mAuthorName = AuthorName;
        mReviewText = ReviewText;
        mReviewLink = ReviewLink;
    }

    public String getmAuthorName(){
        return mAuthorName;
    }
    public String getmReviewText(){
        return mReviewText;
    }

    public String getmReviewLink(){
        return mReviewLink;
    }

}

