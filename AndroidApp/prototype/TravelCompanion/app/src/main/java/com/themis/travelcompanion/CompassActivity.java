package com.themis.travelcompanion;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    ActionBar actionBar;

    TextView Degreestv;

    private ImageView compassiv;

    private float currentdeg = 0f;

    private SensorManager csensorManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Compass");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        compassiv = findViewById(R.id.Compassiv);


        Degreestv = findViewById(R.id.Degreestv);


        csensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        csensorManager.registerListener(this, csensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        csensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        float degree = Math.round(event.values[0]);

        Degreestv.setText("" + Float.toString(degree) + " °");


        RotateAnimation animation = new RotateAnimation(
                currentdeg,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);


        animation.setDuration(210);


        animation.setFillAfter(true);


        compassiv.startAnimation(animation);
        currentdeg = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}