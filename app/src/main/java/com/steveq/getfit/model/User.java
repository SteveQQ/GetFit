package com.steveq.getfit.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class User {
    private String mUserName;
    private String mPassword;
    private ArrayList<Meal> mListMeals;
    private int mAge;
    private String mSex;
    private int mHeight;
    private int mWeight;
    private int mCalories;
    private Calendar timeStamp;


    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
        prepareListData();
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<Meal> getListMeals() {
        return mListMeals;
    }

    public void setListMeals(ArrayList<Meal> listMeals) {
        mListMeals = listMeals;
    }

    private void prepareListData() {

        mListMeals = new ArrayList<>();

        mListMeals.add(new Meal("Breakfast"));
        mListMeals.add(new Meal("Brunch"));
        mListMeals.add(new Meal("Lunch"));
        mListMeals.add(new Meal("Dinner"));
        mListMeals.add(new Meal("Supper"));

    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String sex) {
        mSex = sex;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int calories) {
        mCalories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (mUserName != null ? !mUserName.equals(user.mUserName) : user.mUserName != null)
            return false;
        return mPassword != null ? mPassword.equals(user.mPassword) : user.mPassword == null;

    }

    @Override
    public int hashCode() {
        int result = mUserName != null ? mUserName.hashCode() : 0;
        result = 31 * result + (mPassword != null ? mPassword.hashCode() : 0);
        return result;
    }
}
