package com.example.babybuy;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    ActionBar actionBar;
    RecyclerView recyclerView;
    SearchView searchView;
    DBHelper dbHelper;

    private List<BabyItems> babyItemsList = new LinkedList<>();
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.HomeScreenTitle);

        /*search operation for recyclerView
        searchView = (SearchView) findViewById(R.id.search);
        searchView.clearFocus();
        /searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterItems(newText);
                return true;
            }
        });*/

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_addItem);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItemList);
        dbHelper = new DBHelper(this);

        displayBabyItems();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, AddItems.class);
                startActivity(intent);
            }
        });
    }

    /*method to filter all items and get required item
    private void filterItems(String text) {
        List<BabyItems> filteredList = new LinkedList<>();
        for (BabyItems babyItems: babyItemsList){
            if(babyItems.getDescription().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(babyItems);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No results founds", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.setFilteredList(filteredList);
        }
    }*/

    //method to display items in the recyclerView
    private void displayBabyItems() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerViewAdapter(dbHelper.getAllItems());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    //method to show items after being added
    @Override
    protected void onResume() {
        super.onResume();
        displayBabyItems();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu,menu);
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
                Intent logOutIntent = new Intent(this,MainActivity.class);
                logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logOutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}