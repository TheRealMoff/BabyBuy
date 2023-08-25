package com.example.babybuy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ViewItem extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    EditText editTextDesc, editTextPrice, editTextLoc;
    ImageButton imageButtonUpdate,imageButtonSms, img_Cam, img_Gallery;
    RadioButton radioButton1, radioButton2;
    ActionBar actionBar;
    private List<BabyItems> babyItemsList = new LinkedList<>();
    int id = 0;

    private static final int GALLERY_CODE = 1;
    private static final int CAMERA_CODE = 2;

    private Uri imageUri;
    private Bitmap selectedImage;

    static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.update);
        }
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get id's
        imageView = (ImageView) findViewById(R.id.updateImage);
        editTextDesc = (EditText) findViewById(R.id.updateDescription);
        editTextPrice = (EditText) findViewById(R.id.updatePrice);
        editTextLoc = (EditText) findViewById(R.id.editTextLocation);
        img_Cam = (ImageButton) findViewById(R.id.img_cam);
        img_Gallery = (ImageButton) findViewById(R.id.img_gallery);
        imageButtonUpdate = (ImageButton) findViewById(R.id.updateItem);
        imageButtonSms = (ImageButton) findViewById(R.id.imageButtonSMS);


        radioButton1 = (RadioButton) findViewById(R.id.mark_purchased);
        radioButton2 = (RadioButton) findViewById(R.id.mark_pending);

        radioButton1.setChecked(Update("mark_one"));
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean one_isChecked) {
                saveIntoSharedPrefs("mark_one", one_isChecked);
            }
        });

        radioButton2.setChecked(Update("mark_two"));
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean two_isChecked) {
                saveIntoSharedPrefs("mark_two", two_isChecked);
            }
        });


        String itemDesc = getIntent().getStringExtra("itemDescription");
        editTextDesc.setText(itemDesc);
        String itemPrice = getIntent().getStringExtra("price");
        editTextPrice.setText(itemPrice);

        img_Cam.setOnClickListener(this);
        img_Gallery.setOnClickListener(this);
        imageButtonUpdate.setOnClickListener(this);
        imageButtonSms.setOnClickListener(this);
        //getItem();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //method to return back to Home Screen
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    public void getItem() {
//        //get data from recyclerView
//        if (getIntent().getBundleExtra("itemData") != null) {
//            Bundle bundle = getIntent().getBundleExtra("itemData");
//            id = bundle.getInt("id");
//            editTextDesc.setText(bundle.getString("description"));
//            //editTextPrice.setText(bundle.getString("price"));
//        } else {
//            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

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
            case R.id.imageButtonSMS:
                //delegate operation
                delegateItem();
                break;

            case R.id.updateItem:
                if (imageView == null || editTextDesc.getText().toString().isEmpty() ||
                        editTextPrice.getText().toString().isEmpty()) {

                    Toast.makeText(this, "Please select an image and fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    BabyItems babyItems = new BabyItems(selectedImage, editTextDesc.getText().toString(),
                            Float.parseFloat(editTextPrice.getText().toString()));
                    Toast.makeText(this, "Item not updated successfully", Toast.LENGTH_SHORT).show();

                    if (dbHelper.updateData(babyItems)) {
                        Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, HomeScreen.class);
                        startActivity(intent);
                    } else {
                        imageView.setImageResource(R.mipmap.add_image);
                        editTextDesc.setText("");
                        editTextPrice.setText("");
                        Toast.makeText(this, "Item not updated", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
        }
    }

    public void saveIntoSharedPrefs(String key, boolean value) {

        SharedPreferences sp = getSharedPreferences("mark", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public boolean Update(String key) {
        SharedPreferences sp = getSharedPreferences("mark", MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }

    private void delegateItem(){
        //create sms message with strings

        String itemDesc = editTextDesc.getText().toString();
        String itemPrice = editTextPrice.getText().toString();

        Intent sendSMS = new Intent();
        sendSMS.setAction(Intent.ACTION_SEND);
        sendSMS.putExtra(Intent.EXTRA_TEXT,"Hello, Can You Please Get Me This Item. "+
                "It would really mean a lot to me. The Item I am looking for is..." +
                itemDesc + ". Its price is " + itemPrice + " Thank You..");
        sendSMS.setType("text/plain");

        try {
            startActivity(sendSMS);
        }
        catch(Exception e){
            Toast.makeText(this, "Unable to find sms app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
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

        getMenuInflater().inflate(R.menu.viewitem_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            /*case R.id.deleteItem:
                confirmDialog();
                break;*/

            case R.id.TagLocation:
                //geotag item operation
                if(editTextLoc.getText().toString().length() != 0){
                    Toast.makeText(this, "Maps", Toast.LENGTH_SHORT).show();
                    String itemLoc = editTextLoc.getText().toString();
                    Uri mapsUri = Uri.parse("geo:0,0?q=" + itemLoc);
                    Intent maps = new Intent(Intent.ACTION_VIEW, mapsUri);
                    maps.setPackage("com.google.android.apps.maps");
                    startActivity(maps);
                }else {
                    Toast.makeText(this, "No location provided", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.About:
                //about operation
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.Settings:
                //settings operation
                Intent settings = new Intent(this, SettingsPreference.class);
                startActivity(settings);
                break;
            case R.id.logout:
                //logout operation
                Intent logOutIntent = new Intent(this,MainActivity.class);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*public void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete Item");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBHelper dbHelper = new DBHelper(ViewItem.this);
                dbHelper.deleteOneRow(babyItemsList.get(id));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }*/

}