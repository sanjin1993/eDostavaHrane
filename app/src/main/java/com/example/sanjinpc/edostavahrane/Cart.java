package com.example.sanjinpc.edostavahrane;

import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjinpc.edostavahrane.Common.Common;
import com.example.sanjinpc.edostavahrane.Database.Database;
import com.example.sanjinpc.edostavahrane.Model.Order;
import com.example.sanjinpc.edostavahrane.Model.Request;
import com.example.sanjinpc.edostavahrane.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        //init

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnZavrsiNarudzbu);


        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ako nije prazna lista narudzbi procesiraj dalje
                if (cart.size()>0)
               showAlerDialog();
                else
                    Toast.makeText(Cart.this, "Ko?arica je prazna odaberite nesto iz menija", Toast.LENGTH_SHORT).show();
            }
        });

        loadListFood();
    }

    private void showAlerDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Jos jedak korak!");
        alertDialog.setMessage("Unesite adresu");
        final EditText editAddress = new EditText(Cart.this );

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editAddress.setLayoutParams(lp);
        alertDialog.setView(editAddress);// dodavanje edit teksta u alert dialog !!!

        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request request = new Request(
                        Common.CurentUser.getPhone(),
                        Common.CurentUser.getName(),
                        editAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                //Submit na firebase
                //Korisitt cemo System.CurrentMill za kljuc
                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                //Brisanje kosarice

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Hvala , Narudzba je primljena sti?e na adresu ;)", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }

    private void loadListFood() {
        cart = new Database(this).getCharts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQunatity()));

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position)
    {
        //Uklonit cemo item iz liste narudzbi prema poziciji
        cart.remove(position);
        //poslje toga izbrisati cemo stare podatke iz sqlite baze
        new Database(this).cleanCart();
        //poslje toga updateujemo nove podatke iz liste narudzbi u sqlite bazu
        for (Order item:cart)
        {
            new Database(this).addToCart(item);
        }
        //ponovo ucitaj sve narudzbe
        loadListFood();
    }
}
