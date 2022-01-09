package com.themis.travelcompanion;

import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    Button btnregl, btnlogl;
    EditText usernamelet, passwordlet;
    ActionBar actionBar;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Travel Companion");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        databaseHelper = new DatabaseHelper(this);

        usernamelet = (EditText)findViewById(R.id.usernamelet);
        passwordlet = (EditText)findViewById(R.id.passwordlet);

        btnlogl = (Button)findViewById(R.id.btnlogl);
        btnregl = (Button)findViewById(R.id.btnregl);

        btnregl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btnlogl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernamelet.getText().toString();
                String password = passwordlet.getText().toString();

                Boolean checklogin = databaseHelper.CheckLogin(username, password);
                if(checklogin == true){
                    Intent intent = new Intent(Login.this, SightseeingsList.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}