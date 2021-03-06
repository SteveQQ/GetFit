package com.steveq.getfit.controller.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.steveq.getfit.R;

import com.steveq.getfit.controller.fragment.AccountFragment;
import com.steveq.getfit.controller.fragment.CaloriesFragment;
import com.steveq.getfit.controller.fragment.FoodsSearchFragment;
import com.steveq.getfit.controller.fragment.TodayPlanFragment;
import com.steveq.getfit.model.Meal;
import com.steveq.getfit.model.User;
import com.steveq.getfit.model.UserManager;

import java.util.Calendar;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {


    @BindView(R.id.drawerLayout) DrawerLayout mDrawerLayout;
    @BindView(R.id.drawerListView) ListView mDrawerListView;
    @BindArray(R.array.tabs) String[] titles;

    private static final String TAG_FOOD_SEARCH = "tag_food_search";
    private static final String TAG_ACCOUNT_MANAGEMENT = "account_management";
    private static final String TAG_TODAY_PLAN = "today_plan";
    private static final String TAG_CALORIES_PREFS = "calories_prefs";

    private FragmentManager mFragmentManager;

    private ActionBarDrawerToggle drawerToggle;
    private int currentPosition;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        currentUser = UserManager.getInstance().getCurrentUser();

        if(currentUser.getTimeStamp() != null){
            Calendar timestamp = currentUser.getTimeStamp();
            Calendar currentTime = Calendar.getInstance();

            if(currentTime.get(Calendar.DAY_OF_YEAR) > timestamp.get(Calendar.DAY_OF_YEAR) && currentTime.get(Calendar.YEAR) == timestamp.get(Calendar.YEAR) ||
                    currentTime.get(Calendar.DAY_OF_YEAR) < timestamp.get(Calendar.DAY_OF_YEAR) && currentTime.get(Calendar.YEAR) > timestamp.get(Calendar.YEAR)){
                for(Meal meal : currentUser.getListMeals()){
                    meal.getFoodList().clear();
                }
            }

        }

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
                if(mFragmentManager.getBackStackEntryCount()-1 >= 0) {
                    String fragmentTag = mFragmentManager.getBackStackEntryAt(mFragmentManager.getBackStackEntryCount() - 1).getName();
                    Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTag);

                    if (fragment instanceof TodayPlanFragment) {
                        currentPosition = 0;

                    }
                    if (fragment instanceof FoodsSearchFragment) {
                        currentPosition = 1;

                    }
                    if (fragment instanceof CaloriesFragment) {
                        currentPosition = 2;

                    }
                    if (fragment instanceof AccountFragment) {
                        currentPosition = 3;

                    }

                    setActionBarTitle(currentPosition);
                }
            }
        });


        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(mFragmentManager.getBackStackEntryCount() - 1 > 0) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        UserManager.getInstance().saveUser(currentUser.getUserName(), currentUser);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position < 4) {
                selectFragment(position);
            } else {
                finish();
            }
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
            case 2:
                fragment = new CaloriesFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_CALORIES_PREFS);
                ft.addToBackStack(TAG_CALORIES_PREFS);
                currentPosition = 2;
                break;
            case 3:
                fragment = new AccountFragment();
                ft.replace(R.id.contentFrame, fragment, TAG_ACCOUNT_MANAGEMENT);
                ft.addToBackStack(TAG_ACCOUNT_MANAGEMENT);
                currentPosition = 3;
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

    public void selectFragment(int position, Fragment fragment){

        FragmentTransaction ft = mFragmentManager.beginTransaction();

        switch(position){
            case 1:
                ft.replace(R.id.contentFrame, fragment, TAG_FOOD_SEARCH);
                ft.addToBackStack(TAG_FOOD_SEARCH);
                currentPosition = 1;
                break;
            case 2:
                ft.replace(R.id.contentFrame, fragment, TAG_CALORIES_PREFS);
                ft.addToBackStack(TAG_CALORIES_PREFS);
                currentPosition = 2;
                break;
            case 3:
                ft.replace(R.id.contentFrame, fragment, TAG_ACCOUNT_MANAGEMENT);
                ft.addToBackStack(TAG_ACCOUNT_MANAGEMENT);
                currentPosition = 3;
                break;
            default:
                ft.replace(R.id.contentFrame, fragment, TAG_TODAY_PLAN);
                ft.addToBackStack(TAG_TODAY_PLAN);
                currentPosition = 0;
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        setActionBarTitle(position);

        mDrawerLayout.closeDrawer(mDrawerListView);

    }

    public void setActionBarTitle(int position) {

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(titles[position]);

    }

}
