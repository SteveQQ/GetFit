package com.steveq.getfit.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.FatSecretImplementation.FoodGet;
import com.steveq.getfit.FatSecretImplementation.FoodSearch;
import com.steveq.getfit.R;
import com.steveq.getfit.adapters.FoodSearchAdapter;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.UserManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodsSearchFragment extends Fragment {

    private ListView mListView;
    private TextView emptyTextView;
    private SearchView mSearchView;
    private FoodSearch mFoodSearch;
    private FoodGet mFoodGet;
    private FoodSearchAdapter adapter;
    public static ArrayList<Food> mFoods;
    public static final String MEAL_INDEX = "meal_index";
    public static final String FOOD_SEARCH = "food_search";
    UserManager mUserManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFoods = new ArrayList<>();
        mFoodSearch = new FoodSearch(getActivity());
        mFoodGet = new FoodGet(getActivity());
        mUserManager = UserManager.getInstance();

        View view = inflater.inflate(R.layout.foods_search_list, container, false);
        mListView = (ListView) view.findViewById(R.id.foodsSearchListView);
        emptyTextView = (TextView) view.findViewById(android.R.id.empty);
        setHasOptionsMenu(true);
        mListView.setEmptyView(emptyTextView);
        adapter = new FoodSearchAdapter(getActivity());
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getArguments() != null){
                    Toast.makeText(parent.getContext(), position+"", Toast.LENGTH_SHORT).show();
                    TodayPlanFragment fragment = new TodayPlanFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(TodayPlanFragment.CHOSEN_FOOD, mFoods.get(position));
                    bundle.putInt(MEAL_INDEX, getArguments().getInt(MEAL_INDEX));
                    fragment.setArguments(bundle);

                    //mUserManager.getCurrentUser().getListMeals().get(getArguments().getInt(MEAL_INDEX)).getFoodList().add(mFoods.get(position));

                    try {
                        mFoodGet.foodsGet(mFoods.get(position).getId());
                        String result = null;
                        while(result == null) {
                             result = mFoodGet.getResultJsonString();
                        }
                        prepareFoodEntry(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    FragmentManager fm = getActivity().getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.contentFrame, fragment, TodayPlanFragment.TODAY_PLAN);
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }
            }
        });

        return view;
    }

    private void prepareFoodEntry(String result) {
        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(result);
            JSONObject food = resultJson.getJSONObject("food");
            String name = food.getString("food_name");
            JSONObject servs = food.getJSONObject("servings");
            JSONArray serving = servs.getJSONArray("serving");
            JSONObject servObject = serving.getJSONObject(0);
            String cals = servObject.getString("calories");
            String carbs = servObject.getString("carbohydrate");
            String protein = servObject.getString("protein");
            String fat = servObject.getString("fat");
            mUserManager.getCurrentUser().
                    getListMeals().
                    get(getArguments().getInt(MEAL_INDEX)).
                    getFoodList().
                    add(new Food(name, cals,
                            carbs, protein,
                            fat));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                String jsonResultString = null;
                try {
                    mFoodSearch.foodsSearch(query, 0);
                    while(jsonResultString == null) {
                        jsonResultString = mFoodSearch.getResultJsonString();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prepareFoodsData(jsonResultString);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void prepareFoodsData(String resultJsonString) {
        JSONObject resultJson = null;
        ArrayList<Food> foodsArrayList = new ArrayList<>();
        try {
            resultJson = new JSONObject(resultJsonString);
            JSONObject foods = resultJson.getJSONObject("foods");
            JSONArray food = foods.getJSONArray("food");
            for(int i=0; i < food.length(); i++){
                JSONObject foodInst = food.getJSONObject(i);
                String fullDescription = foodInst.getString("food_description");
                String[] fragmentedDescription = fullDescription.split("\\|");
                Long foodId = Long.valueOf(foodInst.getString("food_id"));
                Food newFood = new Food(
                        foodInst.getString("food_name"),
                        fragmentedDescription[0],
                        fragmentedDescription[1],
                        fragmentedDescription[2],
                        fragmentedDescription[3]);
                newFood.setId(foodId);
                foodsArrayList.add(i, newFood);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mFoods = foodsArrayList;
        adapter.notifyDataSetChanged();
        mSearchView.clearFocus();
    }

}
