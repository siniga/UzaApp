package com.agnet.uza.pages;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.pages.expenses.expenses.ExpensesFragment;
import com.agnet.uza.pages.inventories.InventoryFragment;
import com.agnet.uza.pages.staffs.StaffFragment;
import com.agnet.uza.pages.stores.StoresFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class MenuFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private DatabaseHandler _dbHandler;
    private CardView _inventoryBtn,_newSaleBtn,_ordersBtn,_staffsBtn,_storesBtn, _expensesBtn;
    private Toolbar _homeToolbar,_toolbar;
    private BottomNavigationView _bottomNavigation;
    private FloatingActionButton _fab;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mnu_fragment_menu_fragment, container, false);
        _c = getActivity();

        //initializing
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _newSaleBtn = view.findViewById(R.id.new_sale_btn);
        _inventoryBtn = view.findViewById(R.id.inventory_btn);
        _ordersBtn = view.findViewById(R.id.orders_btn);
        _staffsBtn = view.findViewById(R.id.staffs_btn);
        _storesBtn = view.findViewById(R.id.store_btn);
        _expensesBtn = view.findViewById(R.id.expenses_btn);
        _fab = _c.findViewById(R.id.fab);

        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);
        _fab.setVisibility(View.GONE);
        _toolbar.setTitle("Main Menu");

        //event
        _newSaleBtn.setOnClickListener(this);
        _inventoryBtn.setOnClickListener(this);
        _ordersBtn.setOnClickListener(this);
        _staffsBtn.setOnClickListener(this);
        _storesBtn.setOnClickListener(this);
        _expensesBtn.setOnClickListener(this);


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);

                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.new_sale_btn:
                new FragmentHelper(_c).replace(new HomeFragment() ," HomeFragment", R.id.fragment_placeholder);
                break;
            case R.id.inventory_btn:
                new FragmentHelper(_c).replaceWithbackStack(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                break;
            case R.id.orders_btn:
                //new FragmentHelper(_c).replaceWithbackStack(new OrdersFragment(), "OrdersFragment", R.id.fragment_placeholder);
                Toast.makeText(_c, "Coming soon...", Toast.LENGTH_LONG).show();
                break;
            case R.id.staffs_btn:
                new FragmentHelper(_c).replaceWithbackStack(new StaffFragment(), "StaffFragment", R.id.fragment_placeholder);
                break;
            case R.id.store_btn:
                new FragmentHelper(_c).replaceWithbackStack(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                break;
            case R.id.expenses_btn:
                new FragmentHelper(_c).replaceWithbackStack(new ExpensesFragment(), "ExpensesFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
