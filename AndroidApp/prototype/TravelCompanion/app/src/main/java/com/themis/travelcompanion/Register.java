package com.themis.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    EditText fullnamereg, usernamereg, passwordreg, cpasswordreg;
    Button btnregr, btnlogr;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);
        databaseHelper = new DatabaseHelper(this);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Travel Companion");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        fullnamereg = (EditText)findViewById(R.id.fullnamereg);
        usernamereg = (EditText)findViewById(R.id.usernamereg);
        passwordreg = (EditText)findViewById(R.id.passwordreg);
        cpasswordreg = (EditText)findViewById(R.id.cpasswordreg);
        btnregr = (Button)findViewById(R.id.btnregr);
        btnlogr = (Button)findViewById(R.id.btnlogr);

        btnlogr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        btnregr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = fullnamereg.getText().toString();
                String username = usernamereg.getText().toString();
                String password = passwordreg.getText().toString();
                String cpassword = cpasswordreg.getText().toString();

                if(username.equals("") || password.equals("") || cpassword.equals("") || fullname.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals(cpassword)){
                        Boolean checkusername = databaseHelper.CheckUsername(username);
                        if(checkusername == true){
                            Boolean insert = databaseHelper.Insert(fullname, username, password);
                            if(insert == true){
                                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
                                fullnamereg.setText("");
                                usernamereg.setText("");
                                passwordreg.setText("");
                                cpasswordreg.setText("");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Username already taken", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    }
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
