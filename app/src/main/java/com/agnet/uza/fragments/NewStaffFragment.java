package com.agnet.uza.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Discount;

import java.util.ArrayList;
import java.util.List;


public class NewStaffFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener{


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct;
    private ImageView _photoSelectorBtn;
    private  ProductPhotoSelectorDialog  _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _name, _sellingPrice, _costPrice, _category,_sku,_stock,_discount;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_staff, container, false);
        _c = getActivity();

         //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _name = view.findViewById(R.id.name);
        _sellingPrice = view.findViewById(R.id.selling_price);

        _saveProduct = view.findViewById(R.id.save_product_btn);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);




        //events
//        _spinnerCategory.setOnItemSelectedListener(this);
//        _spinnerDiscount.setOnItemSelectedListener(this);
//        _photoSelectorBtn.setOnClickListener(this);

        _saveProduct.setOnClickListener(this);

//        _saveProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(_c, "hehre", Toast.LENGTH_SHORT).show();
//            }
//        });

        //methods
     ///   getCategories();
        //getDiscounts();


        return view;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void getCategories(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Categories","",1));
        categories.add(new Category(2, "Trousers","",1));
        categories.add(new Category(3, "Pants","",1));
        categories.add(new Category(4, "Sneakers","",1));
        categories.add(new Category(5, "Official Shoes","",1));
        categories.add(new Category(6, "Ties","",1));

        // Creating adapter for spinner
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(_c, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
//        _spinnerCategory.setAdapter(dataAdapter);
    }

    private void getDiscounts(){
        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount("Discounts", 0));
        discounts.add(new Discount("Black friday", 13));
        discounts.add(new Discount("Pasaka offer", 15));

        // Creating adapter for spinner
        ArrayAdapter<Discount> dataAdapter = new ArrayAdapter<Discount>(_c, android.R.layout.simple_spinner_item,  discounts);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
//        _spinnerDiscount.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_product_btn:

                break;
        }
    }

    public void displaySelectedImg(Bitmap img){
       // Log.d("IIICCKDDD", img.toString());
      //  _photoSelectorBtn.setImageBitmap(img);
        _customDialog.dismiss();
    }

}
