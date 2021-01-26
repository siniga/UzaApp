package com.agnet.uza.fragments.inventories;


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
import com.agnet.uza.adapters.CategoryAdapter;
import com.agnet.uza.adapters.ManageCategoryAdapter;
import com.agnet.uza.adapters.StaffsAdapter;
import com.agnet.uza.fragments.ReceiptFragment;
import com.agnet.uza.fragments.categories.NewCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ManageCategoryFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager;
    private RecyclerView _categoryList;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private DatabaseHandler _dbHandler;
    private LinearLayout _newCategoryBtn;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_category, container, false);
        _c = getActivity();

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

        _newCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new NewCategoryFragment(),"NewCategoryFragment", R.id.fragment_placeholder);
            }
        });

        getLocalCategory();
        return view;

    }

    public void getLocalCategory() {

        List<Category> categories = _dbHandler.getCategories();
        ManageCategoryAdapter adapter = new ManageCategoryAdapter(_c, categories);
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
