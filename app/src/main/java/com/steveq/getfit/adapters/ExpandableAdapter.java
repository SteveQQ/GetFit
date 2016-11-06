package com.steveq.getfit.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.steveq.getfit.R;
import com.steveq.getfit.controller.fragment.TodayPlanFragment;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;
import com.steveq.getfit.model.UserManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Fragment mFragment;
    private List<Meal> mMeals;
    private HashMap<Meal, ArrayList<Food>> mFoods;
    private UserManager mUserManager;

    public ExpandableAdapter(Context context, Fragment fragment, List<Meal> meals, HashMap<Meal, ArrayList<Food>> foods) {
        mContext = context;
        mMeals = meals;
        mFoods = foods;
        mFragment = fragment;
        mUserManager = UserManager.getInstance();
    }

    @Override
    public int getGroupCount() {
        return mMeals.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mFoods.get(mMeals.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mMeals.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return mFoods.get(mMeals.get(groupPosition)).get(childPosition);
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
            holder.addFoodButton = (ImageView)convertView.findViewById(R.id.addFoodButton);
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

    public interface childButtonClickable{
        void onChildButtonClick(int groupIndex, Food children);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item_child, null);

            holder = new ChildViewHolder();
            holder.foodName = (TextView) convertView.findViewById(R.id.foodNameTextView);
            holder.caloriesTitle = (TextView) convertView.findViewById(R.id.caloriesTextViewTitle);
            holder.caloriesInfo = (TextView) convertView.findViewById(R.id.caloriesTextViewInfo);
            holder.fatTitle = (TextView) convertView.findViewById(R.id.fatTextViewTitle);
            holder.fatInfo = (TextView) convertView.findViewById(R.id.fatTextViewInfo);
            holder.carbsTitle = (TextView) convertView.findViewById(R.id.carbsTextViewTitle);
            holder.carbsInfo = (TextView) convertView.findViewById(R.id.carbsTextViewInfo);
            holder.proteinsTitle = (TextView) convertView.findViewById(R.id.proteinTextViewTitle);
            holder.proteinsInfo = (TextView) convertView.findViewById(R.id.proteinTextViewInfo);
            holder.removeFoodButton = (ImageView) convertView.findViewById(R.id.deleteFoodButton);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        final Food food = (Food) getChild(groupPosition, childPosition);

        holder.foodName.setText(food.getName());
        holder.caloriesTitle.setText("per " + food.getQuantity() + " g");
        holder.caloriesInfo.setText(food.getCalories() + "kcal");
        holder.fatTitle.setText("Fat: ");
        holder.fatInfo.setText(food.getFat() + "g");
        holder.carbsTitle.setText("Carbo: ");
        holder.carbsInfo.setText(food.getCarbo() + "g");
        holder.proteinsTitle.setText("Protein: ");
        holder.proteinsInfo.setText(food.getProtein() + "g");
        holder.removeFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TodayPlanFragment)mFragment).onChildButtonClick(groupPosition, food);

            }
        });
        return convertView;
    }

    private static class ParentViewHolder{
        TextView mealNameTextView;
        ImageView addFoodButton;
        //Button addFoodButton;
        int mIndex;
    }

    private static class ChildViewHolder{
        TextView foodName;
        TextView caloriesTitle;
        TextView caloriesInfo;
        TextView fatTitle;
        TextView fatInfo;
        TextView carbsTitle;
        TextView carbsInfo;
        TextView proteinsTitle;
        TextView proteinsInfo;
        ImageView removeFoodButton;
    }

}
