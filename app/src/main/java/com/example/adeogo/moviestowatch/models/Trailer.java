package com.example.adeogo.moviestowatch.models;

/**
 * Created by Adeogo on 4/29/2017.
 */

public class Trailer {

    private String mTrailerName;
    private String mVideoUrlPart;

    public Trailer(String TrailerName, String VideoUrlpart){
        mTrailerName = TrailerName;
        mVideoUrlPart = VideoUrlpart;
    }

    public String getmTrailerName(){
        return mTrailerName;
    }
    public String getmVideoUrlPart(){
        return mVideoUrlPart;
    }
}



