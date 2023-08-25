package com.example.babybuy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class AddItems extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    EditText editTextDesc, editTextPrice;
    ImageButton imageButtonSave, img_Cam, img_Gallery;

    ActionBar actionBar;

    private static final int GALLERY_CODE = 1;
    private static final int CAMERA_CODE = 2;

    private Uri imageUri;
    private Bitmap selectedImage;

    static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.addItemTitle);
        }
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);

        imageView = (ImageView) findViewById(R.id.addImage);
        imageButtonSave = (ImageButton) findViewById(R.id.addAct_save);
        img_Cam = (ImageButton) findViewById(R.id.img_cam);
        img_Gallery = (ImageButton) findViewById(R.id.img_gallery);
        editTextDesc = (EditText) findViewById(R.id.addAct_description);
        editTextPrice = (EditText) findViewById(R.id.addAct_Price);

        imageButtonSave.setOnClickListener(this);
        img_Cam.setOnClickListener(this);
        img_Gallery.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //method to return back to Home Screen
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.img_cam:
                //post a photo from the camera
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, CAMERA_CODE);

                break;
            case R.id.img_gallery:
                //post an image
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*"); //anything that is image related
                startActivityForResult(galleryIntent, GALLERY_CODE);

                break;

            case R.id.addAct_save:
                 if (imageView == null || editTextDesc.getText().toString().isEmpty() || editTextPrice.getText().toString().isEmpty()){
                     Toast.makeText(this, "Please select an image and fill all fields", Toast.LENGTH_SHORT).show();
                 }
                    else {
                        BabyItems babyItems = new BabyItems(selectedImage,editTextDesc.getText().toString(),
                                Float.parseFloat(editTextPrice.getText().toString()));
                        if
                         (dbHelper.insertItem(babyItems)) {
                             Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show();
                             Intent intent = new Intent(this, HomeScreen.class);
                             startActivity(intent);
                         } else{
                             imageView.setImageResource(R.mipmap.add_image);
                             editTextDesc.setText("");
                             editTextPrice.setText("");
                             Toast.makeText(this, "Item not added", Toast.LENGTH_SHORT).show();
                         }
                 }
                    break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
             if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null){
                 imageUri = data.getData();
                 selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

                 imageView.setImageBitmap(selectedImage);

             } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {
                 Bundle extras = data.getExtras();
                 Bitmap imageBitmap = (Bitmap) extras.get("data");
                 imageView.setImageBitmap(imageBitmap);
             }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.About:
                //about operation
                Intent intent = new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.Settings:
                //settings operation
                Intent settings = new Intent(this, SettingsPreference.class);
                startActivity(settings);
                break;
            case R.id.logout:
                //logout operation
        }
        return super.onOptionsItemSelected(item);
    }

}