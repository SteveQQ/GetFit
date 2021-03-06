package com.steveq.getfit.controller.fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.steveq.getfit.R;
import com.steveq.getfit.adapters.ExpandableAdapter;
import com.steveq.getfit.controller.activity.MainActivity;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;
import com.steveq.getfit.model.User;
import com.steveq.getfit.model.UserManager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayPlanFragment extends Fragment implements ExpandableAdapter.itemButtonClickable, ExpandableAdapter.childButtonClickable {

    @BindView(R.id.caloriesCounterTextView) TextView caloriesCounterTextView;

    ExpandableAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    ArrayList<Meal> listMeals;
    HashMap<Meal, ArrayList<Food>> mapFoods;

    public static final String CHOSEN_FOOD = "chosen_food";
    public static final String TODAY_PLAN = "today_plan";

    private User currentUser;

    public TodayPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        currentUser = UserManager.getInstance().getCurrentUser();

        View view =  inflater.inflate(R.layout.today_plan_list, container, false);
        ButterKnife.bind(this, view);

        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);

        loadCollections();

        expandableListAdapter = new ExpandableAdapter(getActivity(), this, listMeals, mapFoods);
        expandableListView.setAdapter(expandableListAdapter);

        currentCaloriesSum();

        return view;
    }

    private int currentCaloriesSum() {

        int caloriesSum = 0;

        for (Meal meal : currentUser.getListMeals()) {
            for (Food food : meal.getFoodList()) {
                caloriesSum += Integer.valueOf(food.getCalories());
            }
        }

        caloriesCounterTextView.setText("Cals:" + caloriesSum + "/" + UserManager.getInstance().getCurrentUser().getCalories());
        if(caloriesSum <= UserManager.getInstance().getCurrentUser().getCalories()){
            caloriesCounterTextView.setTextColor(Color.GREEN);
        } else {
            caloriesCounterTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.material_red_300_155));
        }

        return caloriesSum;
    }

    private void loadCollections() {
        listMeals = currentUser.getListMeals();
        mapFoods = new HashMap<>();
        for(Meal meal : currentUser.getListMeals()){
            mapFoods.put(meal, meal.getFoodList());
        }
    }

    @Override
    public void onItemButtonClick(int index) {
        FoodsSearchFragment fragment = new FoodsSearchFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(FoodsSearchFragment.MEAL_INDEX, index);
        fragment.setArguments(bundle);

        ((MainActivity)getActivity()).selectFragment(1, fragment);
    }

    @Override
    public void onPause() {
        super.onPause();
        UserManager.getInstance().saveUser(currentUser.getUserName(), currentUser);
    }

    @Override
    public void onChildButtonClick(int groupIndex, Food children) {

        currentUser.getListMeals().get(groupIndex).getFoodList().remove(children);
        expandableListView.collapseGroup(groupIndex);
        currentCaloriesSum();

    }
}
