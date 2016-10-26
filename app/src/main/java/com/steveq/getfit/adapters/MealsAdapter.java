package com.steveq.getfit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.steveq.getfit.R;
import com.steveq.getfit.model.Meal;

public class MealsAdapter extends BaseAdapter {


    private Context mContext;
    private Meal[] mMeals;

    public MealsAdapter(Context context, Meal[] meals){
        mContext = context;
        mMeals = meals;
    }

    @Override
    public int getCount() {
        return mMeals.length;
    }

    @Override
    public Object getItem(int position) {
        return mMeals[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expandable_list_item, null);
            holder = new ViewHolder();
            holder.mealNameTextView = (TextView) convertView.findViewById(R.id.mealName);
            holder.addFoodButton = (Button)convertView.findViewById(R.id.addFoodButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Meal meal = mMeals[position];

        holder.mealNameTextView.setText(meal.toString());
        holder.addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), meal.toString(), Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder{
        TextView mealNameTextView;
        Button addFoodButton;
    }
}
