package com.agnet.uza.pages.inventories.categories;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.inventories.categories.HomeCategoryAdapter;
import com.agnet.uza.pages.checkout.PaymentFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryFilterFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _categoryLayoutManager;
    private RecyclerView _categoryList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private FloatingActionButton _fab;
    private DatabaseHandler _dbHandler;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hm_fragment_categories, container, false);
        _c = getActivity();

        _dbHandler = new DatabaseHandler(_c);

        //binding
        _categoryList = view.findViewById(R.id.category_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _fab = _c.findViewById(R.id.fab);

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


        HomeCategoryAdapter adapter = new HomeCategoryAdapter(_c, categories);
        _categoryList.setAdapter(adapter);


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
                new FragmentHelper(_c).replaceWithbackStack(new PaymentFragment(), "ReceiptFragment", R.id.fragment_placeholder);
                break;

            default:
                break;
        }
    }
}
