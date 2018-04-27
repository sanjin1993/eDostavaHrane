package com.example.sanjinpc.edostavahrane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjinpc.edostavahrane.Common.Common;
import com.example.sanjinpc.edostavahrane.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {


    Button btnPrijava , btnRegistracija;
    TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPrijava = (Button)findViewById(R.id.btnPrijava);
        btnRegistracija = (Button) findViewById(R.id.btnRegistracija);

        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(), "Fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);

        //Init paper
        Paper.init(this);

        btnPrijava.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent signIn = new Intent(MainActivity.this , SignIn.class);
               startActivity(signIn);
           }
       });

        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(MainActivity.this , SingUp.class);
                startActivity(signUp);
            }
        });

        //provjera da li ima vec logiran korisnik

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (user!=null && pwd!=null) {
            if (!user.isEmpty() && !pwd.isEmpty()) {
                login(user, pwd);
            }
        }
    }

    private void login(final String phone, final String pwd) {
        //isti kod kao i kod klase prijava

        // Inicijalno firebase baza

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Molimo sacekajte...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
    @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               //Provjera da li korinik postoji u bazi

               if (dataSnapshot.child(phone).exists()) {
                   //uzmi korisnikove informacije
                   mDialog.dismiss();
                   User user = dataSnapshot.child(phone).getValue(User.class);

                   user.setPhone(phone);

                   if (user.getPassword().equals(pwd)) {
                       Intent homeIntent = new Intent(MainActivity.this, Home.class);
                       Common.CurentUser = user;
                       startActivity(homeIntent);
                       finish();
                   } else {
                       Toast.makeText(MainActivity.this, "Pogresna lozinka !!", Toast.LENGTH_SHORT).show();
                   }
               } else {
                   mDialog.dismiss();
                   Toast.makeText(MainActivity.this, "Korisnik ne postoji !!", Toast.LENGTH_SHORT).show();
               }
           }


           @Override
           public void onCancelled(DatabaseError databaseError) {

           }

       });
    }
}


