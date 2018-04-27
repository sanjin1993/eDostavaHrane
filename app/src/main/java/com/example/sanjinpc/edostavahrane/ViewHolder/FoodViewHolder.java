package com.example.sanjinpc.edostavahrane.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.sanjinpc.edostavahrane.Interface.ItemClickListener;
import com.example.sanjinpc.edostavahrane.R;

/**
 * Created by SanjinPc on 4/13/2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView food_name;
    public TextView food_description;
    public TextView menuId;
    public ImageView food_image;
    public TextView food_price;
    public TextView food_discount;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name = (TextView) itemView.findViewById(R.id.food_name);
        food_image = (ImageView) itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
