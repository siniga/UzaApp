package com.agnet.uza.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.agnet.uza.R;

import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.ExpensesCategory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class SpashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;
    private Gson _gson;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _preferences = getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        _gson = gsonBuilder.create();

        _dbHandler = new DatabaseHandler(this);

        //if expenses category table is empty add categories
        //by default users should be presented with a list of expense categories when they want to add expenses into their business
        if (_dbHandler.getExpensesCategories().size() == 0) {
            for (ExpensesCategory category : preloadCategories()) {
                _dbHandler.createExpensesCategory(new ExpensesCategory(category.getId(), category.getName(), category.getImgUrl(),0));
            }
        }


        /*  Showing splash screen with a timer. This will be useful when you
         want to show case your app logo / company*/
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            Intent intent = new Intent(SpashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();




        }, SPLASH_TIME_OUT);

    }

    public List<ExpensesCategory> preloadCategories() {
        List<ExpensesCategory> localCategoryList = new ArrayList<>();
        localCategoryList.add(new ExpensesCategory(1, "Vinywaji", "ic_beverages",0));
        localCategoryList.add(new ExpensesCategory(2, "Chakula", "ic_food",0));
        localCategoryList.add(new ExpensesCategory(3, "Dawasco", "ic_water",0));
        localCategoryList.add(new ExpensesCategory(4, "Tanesco", "ic_electricity",0));
        localCategoryList.add(new ExpensesCategory(5, "Vocha", "ic_voucher",0));
        localCategoryList.add(new ExpensesCategory(6, "Gari", "ic_car",0));
        localCategoryList.add(new ExpensesCategory(7, "King'amuzi", "ic_cable_tv",0));
        localCategoryList.add(new ExpensesCategory(8, "Bando", "ic_phone",0));
        localCategoryList.add(new ExpensesCategory(9, "Kodi ya nyumba", "ic_rental",0));
        localCategoryList.add(new ExpensesCategory(10, "Usafiri", "ic_transportation",0));
        localCategoryList.add(new ExpensesCategory(11, "Parking", "ic_parking",0));
        localCategoryList.add(new ExpensesCategory(12, "Gym", "ic_gym",0));
        localCategoryList.add(new ExpensesCategory(13, "Mavazi", "ic_cloth",0));

        return localCategoryList;

    }


}
