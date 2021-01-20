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
import androidx.fragment.app.FragmentTransaction;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Discount;

import java.util.ArrayList;
import java.util.List;


public class NewCategoryFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct;
    private ImageView _photoSelectorBtn;
    private ProductPhotoSelectorDialog _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private EditText _categoryName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _categoryName = view.findViewById(R.id.category_name);
        _saveProduct = view.findViewById(R.id.save_category_btn);

        _dbHandler = new DatabaseHandler(_c);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _saveProduct.setOnClickListener(this);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_category_btn:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new NewProductFragment());
                trans.commit();
                manager.popBackStack();

                if(_categoryName.getText().toString().isEmpty()){
                    new FragmentHelper(_c).replace(new NewProductFragment(),"NewProductFragment",R.id.fragment_placeholder);
                }else{
                    _dbHandler.createCategory(new Category(0, _categoryName.getText().toString(), "", 0));
                    new FragmentHelper(_c).replace(new NewProductFragment(),"NewProductFragment",R.id.fragment_placeholder);
                }

                break;
        }
    }

}
