package com.steveq.getfit.controller.fragment;

import android.accounts.NetworkErrorException;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FoodsSearchFragment extends Fragment {

    @BindView(R.id.foodsSearchListView) ListView mListView;
    @BindView(android.R.id.empty) TextView emptyTextView;
    @BindView(R.id.quantityEditText) EditText quantityEditText;
    private SearchView mSearchView;
    private FoodSearch mFoodSearch;
    private FoodGet mFoodGet;
    private FoodSearchAdapter adapter;
    public static ArrayList<Food> mFoods;
    public static final String MEAL_INDEX = "meal_index";
    private static final String QUANTITY = "quantity";
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
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        adapter = new FoodSearchAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setEmptyView(emptyTextView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getArguments() != null){
                    TodayPlanFragment fragment = new TodayPlanFragment();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(TodayPlanFragment.CHOSEN_FOOD, mFoods.get(position));
                    bundle.putInt(MEAL_INDEX, getArguments().getInt(MEAL_INDEX));
                    fragment.setArguments(bundle);

                    try {
                        mFoodGet.execMethod(mFoodGet.buildRequest(mFoods.get(position).getId()));
                        String result = null;
                        while(result == null) {
                             result = mFoodGet.getResultJsonString();
                        }
                        prepareFoodEntry(result);
                        mUserManager.getCurrentUser().setTimeStamp(Calendar.getInstance());
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
        int factor;
        try {
            if(quantityEditText.getText().toString().equals("")){
                factor = 100;
            } else {
                factor = Integer.valueOf(quantityEditText.getText().toString());
            }
            resultJson = new JSONObject(result);
            JSONObject food = resultJson.getJSONObject("food");
            String name = food.getString("food_name");
            JSONObject servs = food.getJSONObject("servings");
            JSONArray serving = servs.getJSONArray("serving");
            JSONObject servObject = serving.getJSONObject(0);
            Integer cals = Integer.valueOf(servObject.getString("calories")) * factor/100;
            Double carbs = Double.valueOf(servObject.getString("carbohydrate")) * factor/100;
            Double protein = Double.valueOf(servObject.getString("protein")) * factor/100;
            Double fat = Double.valueOf(servObject.getString("fat")) * factor/100;
            Food newFoodEntry = new Food(name, String.valueOf(cals),
                    String.valueOf(Math.round(carbs)), String.valueOf(Math.round(protein)),
                    String.valueOf(Math.round(fat)));
            newFoodEntry.setQuantity(factor);
            mUserManager.getCurrentUser().
                    getListMeals().
                    get(getArguments().getInt(MEAL_INDEX)).
                    getFoodList().
                    add(newFoodEntry);
        } catch (JSONException e) {

            try {
                if(quantityEditText.getText().toString().equals("")){
                    factor = 100;
                } else {
                    factor = Integer.valueOf(quantityEditText.getText().toString());
                }
                resultJson = new JSONObject(result);
                JSONObject food = resultJson.getJSONObject("food");
                String name = food.getString("food_name");
                JSONObject servs = food.getJSONObject("servings");
                JSONObject serving = servs.getJSONObject("serving");
                Integer cals = Integer.valueOf(serving.getString("calories")) * factor/100;
                Double carbs = Double.valueOf(serving.getString("carbohydrate")) * factor/100;
                Double protein = Double.valueOf(serving.getString("protein")) * factor/100;
                Double fat = Double.valueOf(serving.getString("fat")) * factor/100;
                Food newFoodEntry = new Food(name, String.valueOf(cals),
                        String.valueOf(Math.round(carbs)), String.valueOf(Math.round(protein)),
                        String.valueOf(Math.round(fat)));
                newFoodEntry.setQuantity(Integer.valueOf(quantityEditText.getText().toString()));
                mUserManager.getCurrentUser().
                        getListMeals().
                        get(getArguments().getInt(MEAL_INDEX)).
                        getFoodList().
                        add(newFoodEntry);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

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
                String jsonResultString = null;
                    try {
                        mFoodSearch.execMethod(mFoodSearch.buildRequest(query, 0));
                        while (jsonResultString == null) {
                            jsonResultString = mFoodSearch.getResultJsonString();
                        }
                        prepareFoodsData(jsonResultString);
                        mFoodSearch.setResultJsonString(null);
                    } catch (NetworkErrorException nee){
                        nee.printStackTrace();
                    }
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
            for (int i = 0; i < food.length(); i++) {
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
