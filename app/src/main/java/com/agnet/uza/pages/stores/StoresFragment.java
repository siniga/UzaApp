package com.agnet.uza.pages.stores;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.StoresAdapter;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Business;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

public class StoresFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _storeLayoutManager;
    private RecyclerView _storeList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newStoreBtn;
    private DatabaseHandler _dbHandler;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_fragment_stores, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _storeList = view.findViewById(R.id.store_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _newStoreBtn = view.findViewById(R.id.new_store_btn);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);
        _toolbar.setTitle("Your Stores");

        // list setup
        //category list
        _storeList.setHasFixedSize(true);
        _storeLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _storeList.setLayoutManager( _storeLayoutManager );

        //events
        _newStoreBtn.setOnClickListener(this);


        List<Business> stores = _dbHandler.getBusinesses();

        StoresAdapter adapter = new StoresAdapter(_c, stores);
        _storeList.setAdapter(adapter);

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
            case R.id.new_store_btn:
                new FragmentHelper(_c).replace(new NewStoreFragment(),"NewStoreFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
