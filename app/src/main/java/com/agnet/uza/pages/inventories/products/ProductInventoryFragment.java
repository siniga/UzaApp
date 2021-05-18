package com.agnet.uza.pages.inventories.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.pages.inventories.products.adapters.ProductInventoryAdapter;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Product;

import java.util.List;


public class ProductInventoryFragment extends Fragment {

    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private RecyclerView _productList;
    private LinearLayoutManager _productLayoutManager;
    private List<Product> _products;
    private ProductInventoryAdapter _productAdapter;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private LinearLayout _newProductBtn;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inv_fragment_products, container, false);
        _c = getActivity();

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _productList = view.findViewById(R.id.product_list);
        _dbHandler = new DatabaseHandler(_c);
        _newProductBtn = view.findViewById(R.id.new_product_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);


        //popular products lists;
        _productList.setHasFixedSize(true);
        _productList.setLayoutManager(new GridLayoutManager(_c, 2));

        _toolbar.setTitle("Manage Inventory");


        _newProductBtn.setOnClickListener(view1 -> {
            new FragmentHelper(_c).replace(new NewInvProductFragment(), "NewProductFragment", R.id.fragment_placeholder);
        });

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
        _productAdapter = new ProductInventoryAdapter(_c, _dbHandler.getProducts(), new HomeFragment(), 1);
        _productList.setAdapter(_productAdapter);
    }



}
