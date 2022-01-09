package com.themis.travelcompanion;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowDetails extends AppCompatActivity {

    ActionBar actionBar;
    private ImageView sightimageiv;
    private TextView sightnametv, sightcommentstv, sightcoutrytv, sightaddresstv, sightareatv;
    private DatabaseHelper dbHelper;
    private Uri imageUri;
    private String sight_id, dname, dcomments, dcountry, daddress, darea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sightimageiv = findViewById(R.id.sightimageiv);
        sightnametv = findViewById(R.id.sightnametv);
        sightcommentstv = findViewById(R.id.sightcommentstv);
        sightcoutrytv = findViewById(R.id.sightcountrytv);
        sightaddresstv = findViewById(R.id.sightadresstv);
        sightareatv = findViewById(R.id.sightareatv);
        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        sight_id = intent.getStringExtra("sight_id");
        dname = intent.getStringExtra("name");
        dcomments = intent.getStringExtra("comments");
        dcountry = intent.getStringExtra("country");
        daddress = intent.getStringExtra("address");
        darea = intent.getStringExtra("area");
        imageUri = Uri.parse(intent.getStringExtra("image"));


        sightnametv.setText(dname);
        sightcommentstv.setText(dcomments);
        sightcoutrytv.setText(dcountry);
        sightaddresstv.setText(daddress);
        sightareatv.setText(darea);

        if (imageUri.toString().equals("null")) {
            sightimageiv.setImageResource(R.drawable.ic_action_addphoto);
        }
        else {
            sightimageiv.setImageURI(imageUri);
        }


    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}