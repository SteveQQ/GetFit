package com.steveq.getfit.model;

import java.util.ArrayList;
import java.util.List;

public class Meal {

    private String mName;
    private ArrayList<Food> mFoodList;

    public Meal(String name) {
        mName = name;
        mFoodList = new ArrayList<>();
    }

    public void addFood(Food food){
        mFoodList.add(food);
    }

    public void removeFood(Food food){
        mFoodList.remove(food);
    }

    public String toString(){
        return mName;
    }
}
