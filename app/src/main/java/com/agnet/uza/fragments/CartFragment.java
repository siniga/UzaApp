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
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Cart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class
CartFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager, _cartListManager;
    private RecyclerView _categoryList,_cartList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private  DatabaseHandler _dbHandler;
    private LinearLayout _emptyErroMsg;
    private TextView _totalAmntTxt;
    private DecimalFormat _currencyformatter;
    private Button _continueBtn;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        _c = getActivity();
        _cartList = view.findViewById(R.id.cart_list);
        _dbHandler = new DatabaseHandler(_c);
        _currencyformatter = new DecimalFormat("#,###,###");


        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _emptyErroMsg = view.findViewById(R.id.empty_items_msg);
        _totalAmntTxt = view.findViewById(R.id.total_amount_txt);
        _continueBtn = view.findViewById(R.id.continue_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);

        //cart list
        _cartList.setHasFixedSize(true);
        _cartListManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _cartList.setLayoutManager(_cartListManager);

        try {
            if(_dbHandler.isCartEmpty()){
                showEmptyErrorMsg();
            }else{
                hideEmptyErrorMsg();
            }
            CartAdapter adapter = new CartAdapter(_c, _dbHandler.getCart(), this);
            _cartList.setAdapter(adapter);

            if (!_dbHandler.isTableEmpty("carts")) {
                _totalAmntTxt.setText("" + _currencyformatter.format(_dbHandler.getCartTotalAmt()));
            }
        }catch (NullPointerException e){
            e.getMessage();
        }

        _continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new ReceiptFragment(), "ReceiptFragment", R.id.fragment_placeholder);
            }
        });


        return view;

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

    public void showEmptyErrorMsg(){
        _emptyErroMsg.setVisibility(View.VISIBLE);
        _cartList.setVisibility(View.GONE);
    }

    public void hideEmptyErrorMsg(){
        _emptyErroMsg.setVisibility(View.GONE);
        _cartList.setVisibility(View.VISIBLE);
    }

    public void updateTotalAmoutUi(){

        try {
            _totalAmntTxt.setText(" " + _currencyformatter.format(_dbHandler.getCartTotalAmt()));
        }catch (NullPointerException e){
            e.getMessage();
        }
    }
}
