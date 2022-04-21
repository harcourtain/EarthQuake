package com.example.earthquake;

public class Word {
    private double mMagnitude;
    private String mDistance;
    private String mLocation;
    private String mDate;
    private String mTime;
    private String mUrl;
    public Word(double magnitude, String distance, String location, String date, String time,String url){
        mMagnitude=magnitude;
        mLocation=location;
        mDistance=distance;
        mDate=date;
        mTime=time;
        mUrl=url;
    }

    public String getmDistance() {
        return mDistance;
    }

    public String getmTime() {
        return mTime;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
