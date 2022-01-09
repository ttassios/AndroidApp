package com.themis.travelcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SightseeingsList extends AppCompatActivity {

    FloatingActionButton compassbtn;
    FloatingActionButton addfab;
    ActionBar actionBar;
    RecyclerView mRecyclerView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightseeingslist);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Your Saved Sightseeings");
        mRecyclerView = findViewById(R.id.recyclerv);
        databaseHelper = new DatabaseHelper(this);

        showRecord();


        addfab = findViewById(R.id.addfabbtn);
        compassbtn = findViewById(R.id.compassbtn);

        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SightseeingsList.this, AddActivity.class);
                intent.putExtra("editMode", false);
                startActivity(intent);
            }
        });

        compassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SightseeingsList.this, CompassActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showRecord() {
        CustomAdapter adapter = new CustomAdapter(SightseeingsList.this, databaseHelper.getAllData());
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRecord();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
