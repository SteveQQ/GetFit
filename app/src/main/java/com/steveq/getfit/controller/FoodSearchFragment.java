package com.steveq.getfit.controller;


import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.FatSecretImplementation.FoodSearch;
import com.steveq.getfit.R;
import com.steveq.getfit.adapters.FoodSearchAdapter;
import com.steveq.getfit.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodSearchFragment extends ListFragment {


    private Food[] mFoods;
    private FoodSearch mFoodSearch;
    private SearchView mSearchView;

    public FoodSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if(mFoods != null) {
            FoodSearchAdapter adapter = new FoodSearchAdapter(getActivity(), mFoods);
            setListAdapter(adapter);
        }


        //Log.d("fragment", getArguments().getString("food_data"));
        mFoodSearch = new FoodSearch(getActivity());
        Log.d("fragment", "CREATE");
        return  super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListShown(true);
        setHasOptionsMenu(true);
        setEmptyText("No data to present, search something...");
        if(mSearchView!=null) {
            mSearchView.clearFocus();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        Log.d("fragment", "SEARCH VIEW");
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
                foodsArrayList.add(i, new Food(
                        foodInst.getString("food_name"),
                        fragmentedDescription[0],
                        fragmentedDescription[1],
                        fragmentedDescription[2],
                        fragmentedDescription[3]
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mFoods = foodsArrayList.toArray(new Food[1]);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setHasOptionsMenu(true);
    }
}
