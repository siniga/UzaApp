package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.adapters.TransactionAdapter;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Cart;
import com.agnet.uza.models.Date;
import com.agnet.uza.models.Transaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionFragment extends Fragment   implements View.OnClickListener{

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
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        _c = getActivity();
        _cartList = view.findViewById(R.id.cart_list);
        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
       // _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
//        _bottomNavigation.setVisibility(View.GONE);


        //cart list
        _cartList.setHasFixedSize(true);
        _cartListManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _cartList.setLayoutManager(_cartListManager);

        getCart();

        return view;

    }


    public void getCart() {
        List<Transaction> transactions1 = new ArrayList<>();
        List<Transaction> transactions2 = new ArrayList<>();

        transactions1.add(new Transaction("01-oct-2020"));
        transactions1.add(new Transaction("13-nov-2020"));
        transactions1.add(new Transaction("11-dec-2020"));

        List<Date> dates = new ArrayList<>();

        dates.add(new Date(transactions1,true, "03-dec-2020"));
        dates.add(new Date(transactions2,true,"01-dec-2020"));

        TransactionAdapter adapter = new TransactionAdapter(_c, transactions1, dates);
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
