package com.steveq.getfit.adapters;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.controller.TodayPlanFragment;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Fragment mFragment;
    private List<Meal> mMeals;
    private HashMap<Meal, List<Food>> mFoods;

    public ExpandableAdapter(Context context, Fragment fragment, List<Meal> meals, HashMap<Meal,List<Food>> foods) {
        mContext = context;
        mMeals = meals;
        mFoods = foods;
        mFragment = fragment;
    }

    @Override
    public int getGroupCount() {
        return mMeals.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFoods.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mMeals.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        if(mFoods.get(mMeals.get(groupPosition)).size() != 0) {
            return mFoods.get(mMeals.get(groupPosition)).get(childPosition);
        }
        return new Food("empty", null, null, null, null);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        final ParentViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item, null);
            holder = new ParentViewHolder();
            holder.mealNameTextView = (TextView) convertView.findViewById(R.id.mealName);
            holder.addFoodButton = (Button)convertView.findViewById(R.id.addFoodButton);
            convertView.setTag(holder);
        } else {
            holder = (ParentViewHolder)convertView.getTag();
        }

        final Meal meal = (Meal)getGroup(groupPosition);

        holder.mIndex = groupPosition;
        holder.mealNameTextView.setText(meal.toString());
        holder.addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TodayPlanFragment)mFragment).onItemButtonClick(holder.mIndex);
            }
        });

        return convertView;
    }

    public interface itemButtonClickable{
        void onItemButtonClick(int index);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if(getChild(groupPosition, childPosition) != null) {
            ChildViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item_child, null);

                holder = new ChildViewHolder();
                holder.foodName = (TextView) convertView.findViewById(R.id.foodNameTextView);
                holder.calories = (TextView) convertView.findViewById(R.id.caloriesTextView);
                holder.fat = (TextView) convertView.findViewById(R.id.fatTextView);
                holder.carbs = (TextView) convertView.findViewById(R.id.carbsTextView);
                holder.proteins = (TextView) convertView.findViewById(R.id.proteinTextView);

                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            final Food food = (Food) getChild(groupPosition, childPosition);

            holder.foodName.setText(food.getName());
            holder.calories.setText(food.getCalories());
            holder.fat.setText(food.getFat());
            holder.carbs.setText(food.getCarbo());
            holder.proteins.setText(food.getProtein());

        }
        return convertView;
    }

    private static class ParentViewHolder{
        TextView mealNameTextView;
        Button addFoodButton;
        int mIndex;
    }

    private static class ChildViewHolder{
        TextView foodName;
        TextView calories;
        TextView fat;
        TextView carbs;
        TextView proteins;
    }

}
