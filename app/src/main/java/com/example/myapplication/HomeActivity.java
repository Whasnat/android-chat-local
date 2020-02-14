package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

import android.Manifest;

public class HomeActivity extends AppCompatActivity {

    private Button ulogout, contacts_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ulogout = findViewById(R.id.log_out_btn);
        contacts_btn = findViewById(R.id.contacts_btn);

        //CONTACTS Button
        contacts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),FindContactsActivity.class);
                startActivity(intent);
            }
        });

        //LOGOUT Button
        ulogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });

        getPermissions();

    }

    //GET Users Permissions
    public void getPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 1);
        }
    }
}
