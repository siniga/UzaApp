package com.agnet.uza.pages.inventories.categories;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.agnet.uza.adapters.inventories.categories.ManageCategoryAdapter;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

public class CategoryInvFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager;
    private RecyclerView _categoryList;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private DatabaseHandler _dbHandler;
    private LinearLayout _newCategoryBtn;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inv_fragment_category_list, container, false);
        _c = getActivity();


        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _categoryList = view.findViewById(R.id.category_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _newCategoryBtn = view.findViewById(R.id.new_category_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);

        // list setup
        //category list
        _categoryList.setHasFixedSize(true);
        _categoryLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _categoryList.setLayoutManager(_categoryLayoutManager);

        List<Category> categories = _dbHandler.getCategories();
        ManageCategoryAdapter adapter = new ManageCategoryAdapter(_c, categories);
        _categoryList.setAdapter(adapter);

        _newCategoryBtn.setOnClickListener(this);

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
            case R.id.new_category_btn:
                _editor.putInt("NEW_CATEGORY_FLAG",1);
                _editor.commit();
                new FragmentHelper(_c).replace(new NewCategoryFragment(), "NewCategoryFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
