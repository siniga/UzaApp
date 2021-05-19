package com.agnet.uza.pages.expenses.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.DateHelper;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;
import com.agnet.uza.models.ExpensesItem;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Success;
import com.agnet.uza.pages.expenses.categories.CategoryExpFragment;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class EditExpFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private Button _saveExpenseBtn, _deleteExpenseBtn;
    private EditText _name, _amount;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private TextView _nameHdr;
    private int _categoryId;
    private Button _selectCategoryBtn;
    private int _expCategoryId, _businessId;
    private String _category;
    private String _TOKEN;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_fragment_edit_expenses, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);
        _gson = new Gson();

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _name = view.findViewById(R.id.name);
        _amount = view.findViewById(R.id.amount);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _selectCategoryBtn = view.findViewById(R.id.select_category);
        _saveExpenseBtn = view.findViewById(R.id.update_expense_btn);
        _deleteExpenseBtn = view.findViewById(R.id.delete_expense_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);

        int expenseId = _preferences.getInt("EXPENSE_ID", 0);
        ExpensesItem expensesItem = _dbHandler.getExpItems(expenseId);

        _name.setText(expensesItem.getName());
        _amount.setText(expensesItem.getAmount());

        if (_preferences.getString("USER_TOKEN", null) != null) {
            _TOKEN = _preferences.getString("USER_TOKEN", null);
//            Log.d("TOKEN_HERE",""+_TOKEN);
        }

        if (_preferences.getInt("BUSINESS_ID", 0) != 0) {
            _businessId = _preferences.getInt("BUSINESS_ID", 0);
        }

        try {
            _expCategoryId = _preferences.getInt("EXP_CATEGORY_ID", 0);
            _category = _preferences.getString("EXP_CATEGORY_NAME", null);

            _selectCategoryBtn.setText(_category);

        } catch (NullPointerException e) {
            Log.d("Log", e.getMessage().toString());
        }

        if (_preferences.getInt("EXPCATEGORY_ID", 0) != 0) {
            _categoryId = _preferences.getInt("EXPCATEGORY_ID", 0);
        }

        _deleteExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _dbHandler.deleteExpense(expenseId);
                _dbHandler.getExpensesTotalAmtByCategory(expenseId);

                getNewAmount();

                new FragmentHelper(_c).replace(new ExpensesFragment(), "ExpensesItemFragment", R.id.fragment_placeholder);
                Toast.makeText(_c, "Item Deleted!", Toast.LENGTH_LONG).show();
            }
        });

        _saveExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    saveExpenses(new ExpensesItem(expensesItem.getId(), name, amount, date,expensesItem.getServerId(), expensesItem.getCategoryId()));
                    //   new FragmentHelper(_c).replace(new ExpensesFragment(), "ExpensesItemFragment", R.id.fragment_placeholder);
                }
            }


        });

        return view;

    }

    private void getNewAmount() {
        String newTotalAmnt = _dbHandler.getExpensesTotalAmtByCategory(_categoryId);
        _dbHandler.updateExpensesCategoryAmnt(newTotalAmnt, _categoryId);
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

    private void saveExpenses(ExpensesItem expense) {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);
        _saveExpenseBtn.setClickable(false);

        Endpoint.setUrl("expense/update");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Response res = _gson.fromJson(response, Response.class);
                    Log.d("RESPONSE_SESSION", _gson.toJson(res.getSuccess()));
                    if (res.getCode() == 200) {
                        Success success = res.getSuccess();
                        ExpensesItem expenseItem = success.getExpense();
                        //Log.d("RESPONSE_",_gson.toJson(success.getExpense()));


                        _dbHandler.updateExpenseItem(new ExpensesItem(expenseItem.getId(), expenseItem.getName(), expenseItem.getAmount(), expenseItem.getDate(),expenseItem.getServerId(),expenseItem.getCategoryId()));

                        new FragmentHelper(_c).replace(new ExpensesFragment(), "ExpensesFragment", R.id.fragment_placeholder);

                    } else {

                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveExpenseBtn.setClickable(true);


                },
                error -> {
                    _saveExpenseBtn.setClickable(true);
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                    Log.d("RESPONSE_ERROR", "here" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        Log.i("log error", errorString);
                    }

                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + _TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", expense.getName());
                params.put("amount", expense.getAmount());
                params.put("date", "" + expense.getDate());
                params.put("categoryId", "" + expense.getCategoryId());
                params.put("businessId", "" + _businessId);
                params.put("id", "" + expense.getServerId());
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        // postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
