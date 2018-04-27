package com.example.sanjinpc.edostavahrane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sanjinpc.edostavahrane.Common.Common;
import com.example.sanjinpc.edostavahrane.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    MaterialEditText edtTelefon , edtPassword;
    Button btnSign;
    com.rey.material.widget.CheckBox ckbRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtTelefon = (MaterialEditText) findViewById(R.id.edtPhone);

        btnSign = (Button) findViewById(R.id.btnSignIn);
        ckbRemember = (com.rey.material.widget.CheckBox) findViewById(R.id.rememberme);


        //Init Paper
        Paper.init(this);

        // Inicijalno firebase baza

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Spasiti korisnikove login podatke user & password

                if (ckbRemember.isChecked())
                {
                    Paper.book().write(Common.USER_KEY,edtTelefon.getText().toString());
                    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                }

               final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
               mDialog.setMessage("Molimo sacekajte...");
               mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Provjera da li korinik postoji u bazi

                        if (dataSnapshot.child(edtTelefon.getText().toString()).exists()) {
                            //uzmi korisnikove informacije
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtTelefon.getText().toString()).getValue(User.class);

                            user.setPhone(edtTelefon.getText().toString());

                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.CurentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else {
                                Toast.makeText(SignIn.this, "Pogresna lozinka !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Korisnik ne postoji !!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
