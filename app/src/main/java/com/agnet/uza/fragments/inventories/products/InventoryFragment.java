package com.agnet.uza.fragments.inventories.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.InventoryTabsAdapter;
import com.agnet.uza.fragments.MenuFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;


public class InventoryFragment extends Fragment {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private  ViewPager _viewPager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_inventory, container, false);
        _c = getActivity();

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _dbHandler = new DatabaseHandler(_c);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _toolbar.setTitle("Manage Inventory");


        initializeTabLayout(view);

        if (_preferences.getInt("INVENTORY_PAGE_TYPE", 0) == 1) {
            _viewPager.setCurrentItem(1);
            _editor.remove("INVENTORY_PAGE_TYPE");
            _editor.commit();

        }

        return view;

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

                        new FragmentHelper(_c).replace(new MenuFragment(), "MenuFragment", R.id.fragment_placeholder);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initializeTabLayout(View view) {

        //Toast.makeText(_c, "Inarudi hapa", Toast.LENGTH_SHORT).show();
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
      _viewPager = view.findViewById(R.id.view_pager);


        InventoryTabsAdapter tabsAdapter = new InventoryTabsAdapter(_c.getSupportFragmentManager(), tabLayout.getTabCount());
        _viewPager.setAdapter(tabsAdapter);


        _viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                _viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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


}
