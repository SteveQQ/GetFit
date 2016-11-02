package com.steveq.getfit.controller;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.adapters.ExpandableAdapter;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;
import com.steveq.getfit.model.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayPlanFragment extends Fragment implements ExpandableAdapter.itemButtonClickable {

    ExpandableAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    ArrayList<Meal> listMeals;
    HashMap<Meal, ArrayList<Food>> mapFoods;
    public static final String CHOSEN_FOOD = "chosen_food";
    public static final String TODAY_PLAN = "today_plan";
    private UserManager mUserManager;
    @BindView(R.id.caloriesCounterTextView) TextView caloriesCounterTextView;

    public TodayPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUserManager = UserManager.getInstance();

        View view =  inflater.inflate(R.layout.today_plan_list, container, false);
        ButterKnife.bind(this, view);

        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);

        loadCollections();

        expandableListAdapter = new ExpandableAdapter(getActivity(), this, listMeals, mapFoods);
        expandableListView.setAdapter(expandableListAdapter);

        caloriesCounterTextView.setText("Cals:" + currentCaloriesSum() + "/" + mUserManager.getCurrentUser().getCalories());
        if(currentCaloriesSum() <= mUserManager.getCurrentUser().getCalories()){
            caloriesCounterTextView.setTextColor(Color.GREEN);
        }

        return view;
    }

    private int currentCaloriesSum() {

        int caloriesSum = 0;

        for(Meal meal : mUserManager.getCurrentUser().getListMeals()){
            for(Food food : meal.getFoodList()){
                caloriesSum += Integer.valueOf(food.getCalories());
            }
        }

        return caloriesSum;
    }

    private void loadCollections() {
        listMeals = mUserManager.getCurrentUser().getListMeals();
        mapFoods = new HashMap<>();
        for(Meal meal : mUserManager.getCurrentUser().getListMeals()){
            mapFoods.put(meal, meal.getFoodList());
        }
    }

    @Override
    public void onItemButtonClick(int index) {
        FoodsSearchFragment fragment = new FoodsSearchFragment();

        Toast.makeText(getActivity(), index + "", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt(FoodsSearchFragment.MEAL_INDEX, index);
        fragment.setArguments(bundle);

        ((MainActivity)getActivity()).selectFragment(1, fragment);

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.replace(R.id.contentFrame, fragment, FoodsSearchFragment.FOOD_SEARCH);
//        ft.addToBackStack(null);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
//        ((MainActivity)getActivity()).setActionBarTitle(1);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUserManager.saveUser(mUserManager.getCurrentUser().getUserName(), mUserManager.getCurrentUser());
    }
}
