package com.steveq.getfit.controller;


import android.app.Fragment;
import android.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodayPlanFragment extends Fragment implements ExpandableAdapter.itemButtonClickable {


    ExpandableAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<Meal> listMeals;
    HashMap<Meal,List<Food>> mapFoods;
    TextView mEmpty;
    public static final String CHOSEN_FOOD = "chosen_food";
    public static final String TODAY_PLAN = "today_plan";

    public TodayPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.today_plan_list, container, false);
        mEmpty = (TextView) view.findViewById(android.R.id.empty);
        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);

        prepareListData();
        expandableListView.setEmptyView(mEmpty);


        if(getArguments() != null){
            Food food = getArguments().getParcelable(CHOSEN_FOOD);
            Meal meal = listMeals.get(getArguments().getInt(FoodsSearchFragment.MEAL_INDEX));
            List<Food> entry = mapFoods.get(meal);
            entry.add(food);
            mapFoods.put(meal, entry);
        }
        expandableListAdapter = new ExpandableAdapter(getActivity(), this, listMeals, mapFoods);
        expandableListView.setAdapter(expandableListAdapter);

        return view;
    }

    private void prepareListData() {
        listMeals = new ArrayList<>();
        mapFoods = new HashMap<>();

        listMeals.add(new Meal("Breakfast"));
        listMeals.add(new Meal("Brunch"));
        listMeals.add(new Meal("Lunch"));
        listMeals.add(new Meal("Dinner"));
        listMeals.add(new Meal("Supper"));

        mapFoods.put(listMeals.get(0), new ArrayList<Food>());
        mapFoods.put(listMeals.get(1), new ArrayList<Food>());
        mapFoods.put(listMeals.get(2), new ArrayList<Food>());
        mapFoods.put(listMeals.get(3), new ArrayList<Food>());
        mapFoods.put(listMeals.get(4), new ArrayList<Food>());

    }

    @Override
    public void onItemButtonClick(int index) {
        FoodsSearchFragment fragment = new FoodsSearchFragment();

        Toast.makeText(getActivity(), index + "", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt(FoodsSearchFragment.MEAL_INDEX, index);
        fragment.setArguments(bundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.contentFrame, fragment, FoodsSearchFragment.FOOD_SEARCH);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
