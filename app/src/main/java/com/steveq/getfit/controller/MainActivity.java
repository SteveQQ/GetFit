package com.steveq.getfit.controller;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.services.FoodService;
import com.fatsecret.platform.services.RecipeService;
import com.fatsecret.platform.services.RequestBuilder;
import com.fatsecret.platform.services.Response;
import com.steveq.getfit.BuildConfig;
import com.steveq.getfit.FatSecretImplementation.FoodSearch;
import com.steveq.getfit.R;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.UserManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends Activity {


    private static final String TAG_FOOD_SEARCH = "tag_food_search";
    private static final String TAG_ACCOUNT_MANAGEMENT = "account_management";
    private static final String TAG_TODAY_PLAN = "today_plan";
    private static final String TAG_CALORIES_PREFS = "calories_prefs";
    private FragmentManager mFragmentManager;
    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.drawerListView) ListView mDrawerListView;
    @BindArray(R.array.tabs) String[] titles;

    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFragmentManager = getFragmentManager();

        selectFragment(0);

        //-----Drawer Preparation-----//
        mDrawerListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles));
        mDrawerListView.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, //drawer toggle - class that allows to tie up funcionalities of action bar and drawer layout
                R.string.open_drawer, R.string.close_drawer){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(drawerToggle);
        //-----Drawer Preparation-----//

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                String fragmentTag = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
                Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTag);

                if(fragment instanceof  TodayPlanFragment){
                    currentPosition = 0;

                }
                if(fragment instanceof FoodsSearchFragment){
                    currentPosition = 1;

                }
                if(fragment instanceof CaloriesFragment){
                    currentPosition = 4;

                }
                if(fragment instanceof AccountFragment){
                    currentPosition = 5;

                }

                setActionBarTitle(currentPosition);
            }
        });


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectFragment(position);
            Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectFragment(int position){

        Fragment fragment;
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        switch(position){
            case 1:
                fragment = new FoodsSearchFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_FOOD_SEARCH);
                ft.addToBackStack(TAG_FOOD_SEARCH);
                currentPosition = 1;
                break;
            case 4:
                fragment = new CaloriesFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_CALORIES_PREFS);
                ft.addToBackStack(TAG_CALORIES_PREFS);
                currentPosition = 4;
                break;
            case 5:
                fragment = new AccountFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_ACCOUNT_MANAGEMENT);
                ft.addToBackStack(TAG_ACCOUNT_MANAGEMENT);
                currentPosition = 5;
                break;
            default:
                fragment = new TodayPlanFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_TODAY_PLAN);
                ft.addToBackStack(TAG_TODAY_PLAN);
                currentPosition = 0;
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        setActionBarTitle(position);

        mDrawerLayout.closeDrawer(mDrawerListView);

    }

    private void setActionBarTitle(int position) {

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(titles[position]);

    }

}
