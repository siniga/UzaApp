package com.agnet.uza.fragments.expenses.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.fragments.expenses.expenses.ExpensesFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.DateHelper;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class NewFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private Button _saveExpenseBtn;
    private EditText _name, _amount;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private TextView _nameHdr;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_expenses, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _saveExpenseBtn = view.findViewById(R.id.save_expense_btn);
        _nameHdr = view.findViewById(R.id.name_hdr);
        _name = view.findViewById(R.id.name);
        _amount = view.findViewById(R.id.amount);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);


        int expCategoryId = _preferences.getInt("EXPCATEGORY_ID", 0);


        _saveExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_name.getText().toString().isEmpty() || _amount.getText().toString().isEmpty()){
                    Toast.makeText(_c, "Both fields should not be empty!", Toast.LENGTH_LONG).show();
                }else{

                    String name = _name.getText().toString();
                    String amount = _amount.getText().toString();
                    String date = DateHelper.getCurrentDate();


                    _dbHandler.createExpenseItem(new ExpensesItem(0,name, amount,date), expCategoryId);

                    //update total amount on cateogry expenses table
                    String categoryTotalAmnt = _dbHandler.getExpItemsTotalAmt(expCategoryId);
                    _dbHandler.updateExpensesCategoryAmnt(categoryTotalAmnt, expCategoryId);

//                    _nameHdr.setText(""+_dbHandler.getExpItemsTotalAmt(expCategoryId));
                    new FragmentHelper(_c).replace(new ExpensesFragment(), "ExpensesItemFragment", R.id.fragment_placeholder);
                }


            }
        });
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

}
