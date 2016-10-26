package com.steveq.getfit.adapters;

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
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;

import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<Meal> mMeals;
    private List<Food> mFoods;

    public ExpandableAdapter(Context context, List<Meal> meals, List<Food> foods) {
        mContext = context;
        mMeals = meals;
        mFoods = foods;
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
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
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


        final Meal mMeal = (Meal) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView mealNameTextView = (TextView) convertView.findViewById(R.id.mealName);
        Button addFoodButton = (Button) convertView.findViewById(R.id.addFoodButton);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), mMeal.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mealNameTextView.setText(mMeal.toString());
        return convertView;

//        ParentViewHolder holder;
//        if(convertView == null){
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item, null);
//            holder = new ParentViewHolder();
//            holder.mealNameTextView = (TextView) convertView.findViewById(R.id.mealName);
//            holder.addFoodButton = (Button)convertView.findViewById(R.id.addFoodButton);
//            convertView.setTag(holder);
//        } else {
//            holder = (ParentViewHolder)convertView.getTag();
//        }
//
//        final Meal meal = (Meal)getGroup(groupPosition);
//
//        holder.mealNameTextView.setText(meal.toString());
//        holder.addFoodButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), meal.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//
//        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Food mFood = (Food) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item_child, null);
        }
        TextView foodName = (TextView)convertView.findViewById(R.id.foodNameTextView);
        TextView calories = (TextView)convertView.findViewById(R.id.caloriesTextView);
        TextView fat = (TextView)convertView.findViewById(R.id.fatTextView);
        TextView carbs = (TextView)convertView.findViewById(R.id.carbsTextView);
        TextView proteins = (TextView)convertView.findViewById(R.id.proteinTextView);

        foodName.setText(mFood.getName());
        calories.setText(mFood.getCalories());
        fat.setText(mFood.getFat());
        carbs.setText(mFood.getCarbo());
        proteins.setText(mFood.getProtein());

        return convertView;

//        ChildViewHolder holder;
//
//        if(convertView == null){
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item_child, null);
//
//            holder = new ChildViewHolder();
//            holder.foodName = (TextView)convertView.findViewById(R.id.foodNameTextView);
//            holder.calories = (TextView)convertView.findViewById(R.id.caloriesTextView);
//            holder.fat = (TextView)convertView.findViewById(R.id.fatTextView);
//            holder.carbs = (TextView)convertView.findViewById(R.id.carbsTextView);
//            holder.proteins = (TextView)convertView.findViewById(R.id.proteinTextView);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ChildViewHolder)convertView.getTag();
//        }
//
//        final Food food = (Food)getChild(groupPosition, childPosition);
//
//        holder.foodName.setText(food.getName());
//        holder.calories.setText(food.getCalories());
//        holder.fat.setText(food.getFat());
//        holder.carbs.setText(food.getCarbo());
//        holder.proteins.setText(food.getProtein());
//
//
//        return convertView;
    }

    private static class ParentViewHolder{
        TextView mealNameTextView;
        Button addFoodButton;
    }

    private static class ChildViewHolder{
        TextView foodName;
        TextView calories;
        TextView fat;
        TextView carbs;
        TextView proteins;
    }

}
