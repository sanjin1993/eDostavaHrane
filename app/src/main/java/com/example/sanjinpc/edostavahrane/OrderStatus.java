package com.example.sanjinpc.edostavahrane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sanjinpc.edostavahrane.Common.Common;
import com.example.sanjinpc.edostavahrane.Interface.ItemClickListener;
import com.example.sanjinpc.edostavahrane.Model.Category;
import com.example.sanjinpc.edostavahrane.Model.Order;
import com.example.sanjinpc.edostavahrane.Model.Request;
import com.example.sanjinpc.edostavahrane.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");


        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        loadOrders(Common.CurentUser.getPhone());

    }

    private void loadOrders(String phone)
    {
            adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class,R.layout.order_layout,OrderViewHolder.class,requests.orderByChild("phone").equalTo(Common.CurentUser.getPhone().toString())) {
                @Override
                protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                    viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                    viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                    viewHolder.txtOrderPhone.setText(model.getPhone());
                    viewHolder.txtOrderAddress.setText(model.getAddress());
                }
            };
            recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status)
    {
        if (status.equals("0"))
            return "Primljena";
        else if (status.equals("1"))
            return "Na putu";
        else
            return "Odnnjeta";
    }

}
