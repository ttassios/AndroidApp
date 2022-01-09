package com.themis.travelcompanion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class EditActivity extends AppCompatActivity {

    private ImageView sightimageiv;
    private EditText nameet, commentset;
    private TextView sightcountrytv, sightaddresstv, sightareatv;
    Button addbtn;
    ActionBar actionBar;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri;

    private String sight_id, dname, dcomments, dcountry, daddress, darea;
    private boolean editMode = false;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Information");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        sightimageiv = findViewById(R.id.sightimageiv);
        nameet = findViewById(R.id.sightnameet);
        commentset = findViewById(R.id.sightcommentset);
        sightcountrytv = findViewById(R.id.sightcountrytv);
        sightaddresstv = findViewById(R.id.sightadresstv);
        sightareatv = findViewById(R.id.sightareatv);
        addbtn = findViewById(R.id.addbtn);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", editMode);
        sight_id = intent.getStringExtra("sight_id");
        dname = intent.getStringExtra("name");
        dcomments = intent.getStringExtra("comments");
        dcountry = intent.getStringExtra("country");
        daddress = intent.getStringExtra("address");
        darea = intent.getStringExtra("area");
        imageUri = Uri.parse(intent.getStringExtra("image"));

        if (editMode) {

            actionBar.setTitle("Update Information");

            editMode = intent.getBooleanExtra("editMode", editMode);
            sight_id = intent.getStringExtra("sight_id");
            dname = intent.getStringExtra("name");
            dcomments = intent.getStringExtra("comments");
            dcountry = intent.getStringExtra("country");
            daddress = intent.getStringExtra("address");
            darea = intent.getStringExtra("area");
            imageUri = Uri.parse(intent.getStringExtra("image"));

            nameet.setText(dname);
            commentset.setText(dcomments);

            if (imageUri.toString().equals("null")) {
                sightimageiv.setImageResource(R.drawable.ic_action_addphoto);
            }
            else {
                sightimageiv.setImageURI(imageUri);
            }

        }
        else {
            actionBar.setTitle("Add Information");
        }

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        dbHelper = new DatabaseHelper(this);

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
                startActivity(new Intent(EditActivity.this, SightseeingsList.class));
                Toast.makeText(EditActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getData() {
        dname = "" +nameet.getText().toString().trim();
        dcomments = "" +commentset.getText().toString().trim();
        dcountry = ""+sightcountrytv.getText().toString().trim();
        daddress = ""+sightaddresstv.getText().toString().trim();
        darea = ""+sightareatv.getText().toString().trim();


        if (editMode) {

            dbHelper.updateSight(
                    "" +sight_id,
                    "" +dname,
                    "" +dcomments,
                    "" +imageUri
            );
        }
        else {

            dbHelper.insertSight(
                    "" +dname,
                    "" +dcomments,
                    "" +dcountry,
                    ""+daddress,
                    ""+darea,
                    ""+imageUri
            );
        }
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
