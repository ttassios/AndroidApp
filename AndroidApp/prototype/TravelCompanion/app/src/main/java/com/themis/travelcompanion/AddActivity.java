package com.themis.travelcompanion;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private ImageView sightimageiv;
    private EditText nameet, commentset, countryet, addresset, areaet;
    Button addbtn, btnlocation;
    ActionBar actionBar;
    LocationManager locationManager;
    LocationListener locationListener;


    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;


    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    private String dname, dcomments, dcountry, daddress, darea;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add a new sight");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sightimageiv = findViewById(R.id.sightimageiv);
        nameet = findViewById(R.id.sightnameet);
        commentset = findViewById(R.id.sightcommentset);
        countryet = findViewById(R.id.sightcountryet);
        addresset = findViewById(R.id.sightadresset);
        areaet = findViewById(R.id.sightareaet);
        addbtn = findViewById(R.id.addbtn);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        dbHelper = new DatabaseHelper(this);


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean isGPS_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPS_enabled) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    try {

                        Geocoder geocoder = new Geocoder(AddActivity.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(latitude,longitude,1);

                        addresset.setText(addressList.get(0).getAddressLine(0));
                        areaet.setText(addressList.get(0).getAdminArea());
                        countryet.setText(addressList.get(0).getCountryName());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }

            };
        }






        btnlocation = (Button) findViewById(R.id.btnlocation);


        btnlocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(AddActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(AddActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String []{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }


            }
        });



        sightimageiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imagePickDialog();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                startActivity(new Intent(AddActivity.this, SightseeingsList.class));
                Toast.makeText(AddActivity.this, "Add Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData() {
        dname = ""+nameet.getText().toString().trim();
        dcomments = ""+commentset.getText().toString().trim();
        dcountry = ""+countryet.getText().toString().trim();
        daddress = ""+addresset.getText().toString().trim();
        darea = ""+areaet.getText().toString().trim();
        dbHelper.insertSight(
                ""+dname,
                ""+dcomments,
                ""+dcountry,
                ""+daddress,
                ""+darea,
                ""+imageUri
        );

    }

    private void imagePickDialog() {
        String [] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select for image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which == 1) {

                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    }
                    else {
                        pickFromStorage();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromStorage() {
        Intent galleryIntent = new Intent (Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean resultl = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);

        return result && resultl;
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickFromStorage();
                    }
                    else {
                        Toast.makeText(this,"Storage permission required!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, locationListener);
                addresset.setText("Press the button again");
                areaet.setText("Press the button again");
                countryet.setText("Press the button again");

            }

        } else {
            addresset.setText("Access denied");
            areaet.setText("Access denied");
            countryet.setText("Access denied");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK)
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }
            else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    sightimageiv.setImageURI(resultUri);
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(this, "" +error, Toast.LENGTH_SHORT).show();
                }
            }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
