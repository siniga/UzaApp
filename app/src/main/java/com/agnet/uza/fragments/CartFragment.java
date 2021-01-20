package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Cart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager, _cartListManager;
    private RecyclerView _categoryList,_cartList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        _c = getActivity();
        _cartList = view.findViewById(R.id.cart_list);
        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        //cart list
        _cartList.setHasFixedSize(true);
        _cartListManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _cartList.setLayoutManager(_cartListManager);

        getCart();

        return view;

    }


    public void getCart() {
        List<Cart> cartList = new ArrayList<>();
        cartList.add(new Cart("Castle Light", "3,000", "330ml", "can", 4));
        cartList.add(new Cart("Budweiser", "1,000", "330ml", "can", 4));
        cartList.add(new Cart("Uhai", "2,000", "330ml", "Plastic Bottle", 4));
        cartList.add(new Cart("Safari", "3,000", "330ml", "can", 4));

        CartAdapter adapter = new CartAdapter(_c, cartList);
        _cartList.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_btn:
                new FragmentHelper(_c).replaceWithbackStack(new ReceiptFragment(), "ReceiptFragment", R.id.fragment_placeholder);
                break;
            case R.id.view_user_login:
//                new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
