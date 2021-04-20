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
import com.agnet.uza.models.Address;
import com.agnet.uza.models.Business;


public class EditStoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _name, _location;
    private Button _saveStore, _deleteStore;
    private DatabaseHandler _dbHandler;
    private int _selectedBusinessId;

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

        _selectedBusinessId = _preferences.getInt("SELECTED_STORE_ID",0);


        Business business = _dbHandler.getSelectedBusiness(_selectedBusinessId);
        _toolbar.setTitle("Edit "+business.getName());
        _name.setText(business.getName());

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
                String addressName = _location.getText().toString();
                _dbHandler.createAddress(new Address(0,addressName, "","",0));

                //get street last id and store it into business table to show its address
                int lastId = _dbHandler.getLastId("addresses");
                _dbHandler.updateBusiness(new Business(_selectedBusinessId,_name.getText().toString(), lastId));

                new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);

                break;
            case R.id.delete_store_btn:
                _dbHandler.deleteBusiness(_selectedBusinessId);
                new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                Toast.makeText(_c, "Item is deleted!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
