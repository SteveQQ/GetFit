package com.steveq.getfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.steveq.getfit.R;
import com.steveq.getfit.controller.fragment.FoodsSearchFragment;
import com.steveq.getfit.model.Food;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_child_simple, null);

            holder = new ViewHolder();
            holder.foodName = (TextView)convertView.findViewById(R.id.foodNameTextView);
            holder.caloriesTitle = (TextView)convertView.findViewById(R.id.caloriesTextViewTitle);
            holder.fatTitle = (TextView)convertView.findViewById(R.id.fatTextViewTitle);
            holder.carbsTitle = (TextView)convertView.findViewById(R.id.carbsTextViewTitle);
            holder.proteinsTitle = (TextView)convertView.findViewById(R.id.proteinTextViewTitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Food food = FoodsSearchFragment.mFoods.get(position);

        holder.foodName.setText(food.getName());
        holder.caloriesTitle.setText(food.getCalories());
        holder.fatTitle.setText(food.getFat());
        holder.carbsTitle.setText(food.getCarbo());
        holder.proteinsTitle.setText(food.getProtein());

        return convertView;
    }

    private static class ViewHolder{
        TextView foodName;
        TextView caloriesTitle;
        TextView fatTitle;
        TextView carbsTitle;
        TextView proteinsTitle;
    }
}
