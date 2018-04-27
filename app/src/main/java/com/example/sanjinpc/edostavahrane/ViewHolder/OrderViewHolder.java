package com.example.sanjinpc.edostavahrane.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sanjinpc.edostavahrane.Interface.ItemClickListener;
import com.example.sanjinpc.edostavahrane.R;

/**
 * Created by SanjinPc on 4/24/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
        txtOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);
        txtOrderStatus  =(TextView)itemView.findViewById(R.id.order_status);
        txtOrderId = (TextView) itemView.findViewById(R.id.order_id);

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