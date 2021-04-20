package com.agnet.uza.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.InventoryAdapter;
import com.agnet.uza.adapters.OrdersAdapter;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Order;
import com.agnet.uza.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private RecyclerView _orderList;
    private LinearLayoutManager _orderLayoutManager;
    private List<Order> _orders;
    private OrdersAdapter _ordersAdapter;
    private FloatingActionButton _fab;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        _c = getActivity();

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _orderList = view.findViewById(R.id.order_list);
        _fab = _c.findViewById(R.id.fab);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _fab.setVisibility(View.GONE);

        //popular products lists;
        _orderList.setHasFixedSize(true);

        _orderLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _orderList.setLayoutManager(_orderLayoutManager);

        //methods
       // getroducts();

        return view;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();

        _fab.setVisibility(View.GONE);
    }

    public void getroducts() {

/*        _orders = new ArrayList<>();
        _orders.add(new Order(1, "", "11:00", "23443", 1, "11:22", 1, 1, 0, 0));
        _orders.add(new Order(1, "", "11:00", "23443", 1, "11:22", 1, 1, 0, 0));
        _orders.add(new Order(1, "", "11:00", "23443", 1, "11:22", 1, 1, 0, 0));
        _orders.add(new Order(1, "", "11:00", "23443", 1, "11:22", 1, 1, 0, 0));

        _ordersAdapter = new OrdersAdapter(_c, _orders);
        _orderList.setAdapter(_ordersAdapter);*/
    }


    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


}
