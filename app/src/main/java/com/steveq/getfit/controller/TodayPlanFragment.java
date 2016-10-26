package com.steveq.getfit.controller;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.adapters.ExpandableAdapter;
import com.steveq.getfit.adapters.MealsAdapter;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TodayPlanFragment extends Fragment {


    Meal[] mMeals ;
    ExpandableAdapter listAdapter;
    ExpandableListView expListView;
    List<Meal> listMeals;
    HashMap<Meal, List<Food>> listFoods;

    public TodayPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.today_plan_list, container, false);
        expListView = (ExpandableListView)v.findViewById(R.id.expandable_view);
        prepareListData();
//        mMeals = new Meal[]{new Meal("Breakfast"), new Meal("Brunch"), new Meal("Lunch"), new Meal("Dinner"), new Meal("Supper")};
//        MealsAdapter adapter = new MealsAdapter(inflater.getContext(), mMeals);
//        setListAdapter(adapter);

        listAdapter = new ExpandableAdapter(getActivity(), listMeals, listFoods);
        expListView.setAdapter(listAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void prepareListData() {
        listMeals = new ArrayList<Meal>();
        listFoods = new HashMap<Meal,List<Food>>();

        listMeals.add(new Meal("Breakfast"));
        listMeals.add(new Meal("Brunch"));
        listMeals.add(new Meal("Lunch"));
        listMeals.add(new Meal("Dinner"));
        listMeals.add(new Meal("Supper"));

        List<Food> breakfast = new ArrayList<Food>();
        breakfast.add(new Food("a", "b", "c", "d", "e"));

        List<Food> brunch = new ArrayList<Food>();
        brunch.add(new Food("a", "b", "c", "d", "e"));

        List<Food> lunch = new ArrayList<Food>();
        lunch.add(new Food("a", "b", "c", "d", "e"));

        List<Food> dinner = new ArrayList<Food>();
        dinner.add(new Food("a", "b", "c", "d", "e"));

        List<Food> supper = new ArrayList<Food>();
        supper.add(new Food("a", "b", "c", "d", "e"));

        listFoods.put(listMeals.get(0), breakfast);
        listFoods.put(listMeals.get(1), brunch);
        listFoods.put(listMeals.get(2), lunch);
        listFoods.put(listMeals.get(3), dinner);
        listFoods.put(listMeals.get(4), supper);
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        Toast.makeText(v.getContext(), mMeals[position].toString(), Toast.LENGTH_SHORT).show();
//        FoodSearchFragment foodSearchFragment = new FoodSearchFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.contentFrame, foodSearchFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//        super.onListItemClick(l, v, position, id);
//    }

}
