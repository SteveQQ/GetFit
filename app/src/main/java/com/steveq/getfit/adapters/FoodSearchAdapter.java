package com.steveq.getfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.controller.FoodsSearchFragment;
import com.steveq.getfit.model.Food;
import com.steveq.getfit.model.Meal;

import java.util.ArrayList;

public class FoodSearchAdapter extends BaseAdapter {

    private Context mContext;

    public FoodSearchAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return FoodsSearchFragment.mFoods.size();
    }

    @Override
    public Object getItem(int position) {
        return FoodsSearchFragment.mFoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item_child, null);

            holder = new ViewHolder();
            holder.foodName = (TextView)convertView.findViewById(R.id.foodNameTextView);
            holder.calories = (TextView)convertView.findViewById(R.id.caloriesTextView);
            holder.fat = (TextView)convertView.findViewById(R.id.fatTextView);
            holder.carbs = (TextView)convertView.findViewById(R.id.carbsTextView);
            holder.proteins = (TextView)convertView.findViewById(R.id.proteinTextView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Food food = FoodsSearchFragment.mFoods.get(position);

        holder.foodName.setText(food.getName());
        holder.calories.setText(food.getCalories());
        holder.fat.setText(food.getFat());
        holder.carbs.setText(food.getCarbo());
        holder.proteins.setText(food.getProtein());

        return convertView;
    }

    private static class ViewHolder{
        TextView foodName;
        TextView calories;
        TextView fat;
        TextView carbs;
        TextView proteins;
    }
}