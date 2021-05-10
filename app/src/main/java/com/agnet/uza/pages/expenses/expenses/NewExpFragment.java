package com.agnet.uza.pages.expenses.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.pages.MenuFragment;
import com.agnet.uza.pages.expenses.categories.CategoryExpFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.DateHelper;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class NewExpFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private Button _saveExpenseBtn;
    private EditText _name, _amount;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private TextView _nameHdr;
    private Button _selectCategoryBtn;
    private int _expCategoryId;
    private String  _persistanNameInput;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_fragment_new_expenses, container, false);
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
        _selectCategoryBtn = view.findViewById(R.id.select_category);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);

        try {
            _expCategoryId = _preferences.getInt("EXP_CATEGORY_ID", 0);
            String category = _preferences.getString("EXP_CATEGORY_NAME", null);

            _selectCategoryBtn.setText(category);

            if (!_preferences.getString("PERSISTENT_NAME", null).isEmpty()) {
                _name.setText(_preferences.getString("PERSISTENT_NAME", null));
                _amount.setText(_preferences.getString("PERSISTENT_AMOUNT", null));
            }


        } catch (NullPointerException e) {
           Log.d("Log",e.getMessage().toString());
        }

        _saveExpenseBtn.setOnClickListener(view1 -> {

            String name = _name.getText().toString();
            String amount = _amount.getText().toString();
            String date = DateHelper.getCurrentDate();

            if (name.isEmpty()) {
                Toast.makeText(_c, "Ingiza jina la matumizi", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty()) {
                Toast.makeText(_c, "Ingiza kiasi cha matumizi!", Toast.LENGTH_SHORT).show();
            } else if (_expCategoryId == 0) {
                Toast.makeText(_c, "Chagua aina ya matumizi", Toast.LENGTH_SHORT).show();
            } else {

                _dbHandler.createExpenseItem(new ExpensesItem(0, name, amount, date), _expCategoryId);
                //update total amount on cateogry expenses table

                //      _nameHdr.setText(""+_dbHandler.getExpItemsTotalAmt(_expCategoryId));

/*
                String categoryTotalAmnt = _dbHandler.getExpItemsTotalAmt(_expCategoryId);
                _dbHandler.updateExpensesCategoryAmnt(categoryTotalAmnt, _expCategoryId);*/

                cleanInputs();
                new FragmentHelper(_c).replace(new ExpensesFragment(), "ExpensesItemFragment", R.id.fragment_placeholder);
            }


        });

        _selectCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String persistanNameInput = _name.getText().toString();
                String persistanAmountInput = _amount.getText().toString();

                _editor.putString("PERSISTENT_NAME",persistanNameInput);
                _editor.putString("PERSISTENT_AMOUNT",persistanAmountInput);
                _editor.commit();

                new FragmentHelper(_c).replace(new CategoryExpFragment(), "CategoryExpFragment", R.id.fragment_placeholder);

            }
        });
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
                        cleanInputs();
                        new FragmentHelper(_c).replace(new ExpensesFragment(), " ExpensesFragmentt", R.id.fragment_placeholder);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void cleanInputs(){
        _editor.remove("EXP_CATEGORY_ID");
        _editor.remove("EXP_CATEGORY_NAME");
        _editor.remove("PERSISTENT_NAME");
        _editor.remove("PERSISTENT_AMOUNT");
        _editor.commit();
    }

}
