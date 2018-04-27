package com.example.sanjinpc.edostavahrane;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.sanjinpc.edostavahrane.Interface.ItemClickListener;
import com.example.sanjinpc.edostavahrane.Model.Category;
import com.example.sanjinpc.edostavahrane.Model.Food;
import com.example.sanjinpc.edostavahrane.ViewHolder.FoodViewHolder;
import com.example.sanjinpc.edostavahrane.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryId = "";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    //Pretraga
    FirebaseRecyclerAdapter<Food,FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");


        recycler_view = (RecyclerView) findViewById(R.id.recycler_food);
        recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);


        //get intent ovdje uzimamo categoryId

        if (getIntent() !=  null)
            categoryId = getIntent().getStringExtra("CategoryId");

        if (!categoryId.isEmpty() && categoryId != null)
        {
            loadListFood(categoryId);
        }

        //Pretraga

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Unesite svoju pretragu");
        
        loadSuggest();
        
      materialSearchBar.setLastSuggestions(suggestList);
      materialSearchBar.setCardViewElevation(10);
      materialSearchBar.addTextChangeListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              //kada korisnik unese tekst suggest lista dobiva novi item
                List<String> suggest = new ArrayList<String>();
                for (String serach:suggestList)//provrti suggestList listu
                {
                    if (serach.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(serach);
                }
                materialSearchBar.setLastSuggestions(suggest);
          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });
      materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
          @Override
          public void onSearchStateChanged(boolean enabled) {
              //Kad je search bar zatvvoren vrati orginalni adapter
              if (!enabled)
                  recycler_view.setAdapter(adapter);

          }

          @Override
          public void onSearchConfirmed(CharSequence text) {
                //kad je pretraga gotova prikazi rezultat kao adapter
              startSearch(text);
          }

          @Override
          public void onButtonClicked(int buttonCode) {

          }
      });
    }

    private void startSearch(CharSequence text)
    {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString())) {//ime hrane je jednako tekstu unesenom u search bar
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",searchAdapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }
        };

        recycler_view.setAdapter(searchAdapter);
    }


    private void loadSuggest()
    {
        foodList.orderByChild("MenuId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());//dodavanje imena hrane do suggestion listi
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

           }
        });
    }

    private void loadListFood(String categoryId)
    {
            adapter = new FirebaseRecyclerAdapter<Food,FoodViewHolder>(Food.class ,
                    R.layout.food_item ,
                    FoodViewHolder.class ,
                    foodList.orderByChild("MenuId").equalTo(categoryId))
            {

                @Override
                protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                    viewHolder.food_name.setText(model.getName());
                    Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_image);

                    final Food local = model;

                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                            foodDetail.putExtra("FoodId",adapter.getRef(position).getKey());
                            startActivity(foodDetail);
                        }
                    });

                }

            };
        recycler_view.setAdapter(adapter);
    }

}
