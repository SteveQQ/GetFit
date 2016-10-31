package com.steveq.getfit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
    private String mUserName;
    private String mPassword;
    private ArrayList<Meal> mListMeals;


    public User(String userName, String password) {
        mUserName = userName;
        mPassword = password;
        prepareListData();
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
