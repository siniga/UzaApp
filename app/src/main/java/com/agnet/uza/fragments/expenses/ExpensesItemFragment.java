package com.agnet.uza.fragments.expenses;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.ExpensesAdapter;
import com.agnet.uza.models.Expense;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExpensesItemFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _expensesLayoutManager;
    private RecyclerView _expensesList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        _c = getActivity();

        //binding
        _expensesList = view.findViewById(R.id.expenses_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        // list setup
        //category list
        _expensesList.setHasFixedSize(true);
        _expensesLayoutManager= new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _expensesList.setLayoutManager( _expensesLayoutManager );



        getLocalStores();

        return view;

    }

    public void getLocalStores() {

        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense(1,"Umeme","23,333",2));
        expenses.add(new Expense(1,"Umeme","10,000",2));
        expenses.add(new Expense(1,"Umeme","14,000",2));
        expenses.add(new Expense(1,"Umeme","3,000",2));
        ExpensesAdapter adapter = new ExpensesAdapter(_c, expenses);
        _expensesList.setAdapter(adapter);
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
}
