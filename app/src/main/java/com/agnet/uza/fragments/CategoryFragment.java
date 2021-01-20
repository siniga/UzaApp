package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.adapters.CategoryAdapter;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Cart;
import com.agnet.uza.models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager;
    private RecyclerView _categoryList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        _c = getActivity();

        //binding
        _categoryList = view.findViewById(R.id.category_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        // list setup
        //category list
        _categoryList.setHasFixedSize(true);
        _categoryLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _categoryList.setLayoutManager(_categoryLayoutManager);


        getLocalCategory();
        return view;

    }

    public void getLocalCategory() {

        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "Beer", "ic_beer_bottle", 1));
        categories.add(new Category(1, "Whiskey", "whiskey", 1));
        categories.add(new Category(1, "Wine", "ic_wine_bottle", 1));
        categories.add(new Category(1, "Gin", "ic_gin_bottle", 1));
        categories.add(new Category(1, "Soda", "ic_soda_bottle", 1));
        categories.add(new Category(1, "Juice", "ic_juice_bottle", 1));

        CategoryAdapter adapter = new CategoryAdapter(_c, categories);
        _categoryList.setAdapter(adapter);
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
