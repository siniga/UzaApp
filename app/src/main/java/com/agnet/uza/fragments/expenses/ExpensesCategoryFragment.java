package com.agnet.uza.fragments.expenses;


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
import com.agnet.uza.adapters.ExpensesCategoryAdapter;
import com.agnet.uza.fragments.ReceiptFragment;
import com.agnet.uza.fragments.expenses.NewExpenseCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Expense;
import com.agnet.uza.models.ExpensesCategory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ExpensesCategoryFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _expensesLayoutManager;
    private RecyclerView _expensesList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newBtn;
    private DatabaseHandler _dbHandler;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses_category, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _expensesList = view.findViewById(R.id.expenses_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _newBtn = view.findViewById(R.id.new_expense_category_btn);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        // list setup
        //category list
        _expensesList.setHasFixedSize(true);
        _expensesLayoutManager= new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _expensesList.setLayoutManager( _expensesLayoutManager );

        //events
        _newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new NewExpenseCategoryFragment(), "NewExpenseCategoryFragment", R.id.fragment_placeholder);
            }
        });


        getLocalStores();

        return view;

    }

    public void getLocalStores() {

        List<ExpensesCategory> expenses = _dbHandler.getExpensesCategories();

        ExpensesCategoryAdapter adapter = new ExpensesCategoryAdapter(_c, expenses);
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
