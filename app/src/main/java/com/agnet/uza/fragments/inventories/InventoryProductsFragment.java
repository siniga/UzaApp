package com.agnet.uza.fragments.inventories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CategoryProductAdapter;
import com.agnet.uza.adapters.InventoryAdapter;
import com.agnet.uza.adapters.InventoryTabsAdapter;
import com.agnet.uza.adapters.ProductAdapter;
import com.agnet.uza.adapters.UnitAdapter;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.helpers.AppManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.helpers.StatusBarHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.ResponseData;
import com.agnet.uza.models.Unit;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class InventoryProductsFragment extends Fragment {

    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private RecyclerView _productList;
    private LinearLayoutManager _productLayoutManager;
    private List<Product> _products;
    private InventoryAdapter _productAdapter;
    private FloatingActionButton _fab;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_products, container, false);
        _c = getActivity();

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _productList = view.findViewById(R.id.product_list);
        _dbHandler = new DatabaseHandler(_c);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);


        //popular products lists;
        _productList.setHasFixedSize(true);
        _productList.setLayoutManager(new GridLayoutManager(_c, 2));

        _toolbar.setTitle("Manage Inventory");


        //methods
        getroducts();
        return view;

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();

    }

    public void getroducts() {
        _productAdapter = new InventoryAdapter(_c, _dbHandler.getProducts(), new HomeFragment(), 1);
        _productList.setAdapter(_productAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}
