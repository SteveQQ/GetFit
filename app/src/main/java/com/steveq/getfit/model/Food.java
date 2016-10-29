package com.steveq.getfit.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable{

    private String mName;
    private String mCalories;
    private String mCarbo;
    private String mProtein;
    private String mFat;

    public Food(String name, String calories, String carbo, String protein, String fat) {
        mName = name;
        mCalories = calories;
        mCarbo = carbo;
        mProtein = protein;
        mFat = fat;
    }

    public String getName() {
        return mName;
    }

    public String getCalories() {
        return mCalories;
    }

    public String getCarbo() {
        return mCarbo;
    }

    public String getProtein() {
        return mProtein;
    }

    public String getFat() {
        return mFat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mCalories);
        dest.writeString(mCarbo);
        dest.writeString(mProtein);
        dest.writeString(mFat);
    }

    private Food(Parcel in){
        mName = in.readString();
        mCalories = in.readString();
        mCarbo = in.readString();
        mProtein = in.readString();
        mFat = in.readString();
    }

}
