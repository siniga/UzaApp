package com.agnet.uza.pages.inventories.categories;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.agnet.uza.R;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.pages.checkout.PaymentFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.pages.inventories.categories.adapters.FilterCategoryAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFilterFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private Gson _gson;
    private GridLayoutManager _categoryLayoutManager;
    private RecyclerView _categoryList;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private FloatingActionButton _fab;
    private DatabaseHandler _dbHandler;
    private LinearLayout _cancelFragment;

    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hm_fragment_categories, container, false);
        _c = getActivity();

        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _categoryList = view.findViewById(R.id.category_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _fab = _c.findViewById(R.id.fab);
        _cancelFragment = view.findViewById(R.id.toolbar_icon);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        // list setup
        //category list
        _categoryList.setHasFixedSize(true);

        _categoryLayoutManager = new GridLayoutManager(_c, 3);
        _categoryList.setLayoutManager(_categoryLayoutManager);

        Thread thread = new Thread(() -> {
//               //
            List<Category> categories = _dbHandler.getCategories();
            _c.runOnUiThread(() -> {

                FilterCategoryAdapter adapter = new FilterCategoryAdapter(_c, categories);
                _categoryList.setAdapter(adapter);
            });
        });
        thread.start();

        _cancelFragment.setOnClickListener(view1 -> {
            _editor.remove("CATEGORY_ID");
            _editor.commit();

            new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);

        });


        return view;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_btn:
                new FragmentHelper(_c).replaceWithbackStack(new PaymentFragment(), "ReceiptFragment", R.id.fragment_placeholder);
                break;

            default:
                break;
        }
    }
}
