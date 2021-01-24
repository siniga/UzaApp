package com.agnet.uza.fragments.staffs;


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
import com.agnet.uza.adapters.StaffsAdapter;
import com.agnet.uza.fragments.ReceiptFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StaffFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _staffLayoutManager;
    private RecyclerView _staffList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newStaffBtn;
    private DatabaseHandler _dbHandler;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staffs, container, false);
        _c = getActivity();

        _dbHandler = new DatabaseHandler(_c);

        //binding
        _staffList = view.findViewById(R.id.staff_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _newStaffBtn = view.findViewById(R.id.new_staff_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        // list setup
        //category list
        _staffList.setHasFixedSize(true);
        _staffLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _staffList.setLayoutManager(_staffLayoutManager);

        _newStaffBtn.setOnClickListener(this);


        getLocalCategory();
        return view;

    }

    public void getLocalCategory() {

        List<User> staffs = _dbHandler.getUsers();


        StaffsAdapter adapter = new StaffsAdapter(_c, staffs);
        _staffList.setAdapter(adapter);
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
            case R.id.new_staff_btn:
                new FragmentHelper(_c).replace(new NewStaffFragment() ,"NewStaffFragmen", R.id.fragment_placeholder);
                break;
            case R.id.view_user_login:
//                new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
