package com.agnet.uza.fragments.stores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Discount;
import com.agnet.uza.models.Store;
import com.agnet.uza.models.Street;

import java.util.ArrayList;
import java.util.List;


public class EditStoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _name, _location;
    private Button _saveStore, _deleteStore;
    private DatabaseHandler _dbHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_store, container, false);
        _c = getActivity();

        //initialize
        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _name = view.findViewById(R.id.name);
        _location = view.findViewById(R.id.location);
        _saveStore = view.findViewById(R.id.update_store_btn);
        _deleteStore = view.findViewById(R.id.delete_store_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _name.setText(_dbHandler.getSelectedStore(_preferences.getInt("SELECTED_STORE",0)).getName());
        _location.setText(_dbHandler.getSelectedStore(_preferences.getInt("SELECTED_STORE",0)).getStreet().getName());

        //events
        _saveStore.setOnClickListener(this);
        _deleteStore.setOnClickListener(this);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                        return true;
                    }
                }
                return false;
            }
        });
    }



    private void getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Categories", "", 1));
        categories.add(new Category(2, "Trousers", "", 1));
        categories.add(new Category(3, "Pants", "", 1));
        categories.add(new Category(4, "Sneakers", "", 1));
        categories.add(new Category(5, "Official Shoes", "", 1));
        categories.add(new Category(6, "Ties", "", 1));

        // Creating adapter for spinner
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(_c, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
//        _spinnerCategory.setAdapter(dataAdapter);
    }

    private void getDiscounts() {
        List<Discount> discounts = new ArrayList<>();
        discounts.add(new Discount("Discounts", 0));
        discounts.add(new Discount("Black friday", 13));
        discounts.add(new Discount("Pasaka offer", 15));

        // Creating adapter for spinner
        ArrayAdapter<Discount> dataAdapter = new ArrayAdapter<Discount>(_c, android.R.layout.simple_spinner_item, discounts);

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
        switch (view.getId()) {
            case R.id.update_store_btn:
                _dbHandler.createStreet(_location.getText().toString());

                //get street last id and store it into business table to show its address
                int lastId = _dbHandler.getLastId("streets");
                _dbHandler.createBusiness(new Store(0,_name.getText().toString(), new Street(lastId,"")));

                new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);

                break;
            case R.id.delete_store_btn:
                _dbHandler.deleteStore(_preferences.getInt("SELECTED_STORE",0));
                new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                Toast.makeText(_c, "Item is deleted!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
