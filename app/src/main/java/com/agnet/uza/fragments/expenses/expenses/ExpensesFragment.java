package com.agnet.uza.fragments.expenses.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.ExpensesAdapter;
import com.agnet.uza.adapters.ExpensesCategoryAdapter;
import com.agnet.uza.fragments.MenuFragment;
import com.agnet.uza.fragments.categories.EditCategoryFragment;
import com.agnet.uza.fragments.expenses.categories.CategoryFragment;
import com.agnet.uza.fragments.expenses.categories.NewCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;
import com.agnet.uza.models.ExpensesItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.text.DecimalFormat;
import java.util.List;

public class ExpensesFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _expensesLayoutManager;
    private RecyclerView _expensesList;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _addExpenseBtn, _addCategoryBtn;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DiscreteScrollView _categoryList;
    private DecimalFormat _currencyformatter;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _currencyformatter = new DecimalFormat("#,###,###");

        //binding
        _expensesList = view.findViewById(R.id.expenses_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _categoryList = view.findViewById(R.id.category_picker);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _addExpenseBtn = view.findViewById(R.id.add_new_expenses_btn);
        _addCategoryBtn = view.findViewById(R.id.add_new_category_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);

        // list setup
        //category list
        _expensesList.setHasFixedSize(true);
        _expensesLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _expensesList.setLayoutManager(_expensesLayoutManager);

     /*   int expCategoryId = _preferences.getInt("EXPCATEGORY_ID", 0);
        _toolbar.setTitle(_preferences.getString("EXPCATEGORY_NAME", null));

*/
        List<ExpensesCategory> categories = _dbHandler.getExpensesCategories();
        ExpensesCategoryAdapter adapter = new ExpensesCategoryAdapter(_c, categories, this);
        _categoryList.setAdapter(adapter);
        _categoryList.scrollToPosition(1);
        _categoryList.setItemTransformer((item, position) -> {
            Toast.makeText(_c, "" + item + " " + position, Toast.LENGTH_SHORT).show();
          /*  final ExpensesCategory currentCategory = categories.get(position);
            List<ExpensesItem> expenses = _dbHandler.getExpItemsByCategory(1);*/
        });


        _categoryList.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());

        _categoryList.addOnItemChangedListener((viewHolder, position) -> {
            final ExpensesCategory currentCategory = categories.get(position);

            loadExpenseAdapter(currentCategory.getId());
            TextView amnt = viewHolder.itemView.findViewById(R.id.amount);
            amnt.setVisibility(View.VISIBLE);
            amnt.setText("" + currentCategory.getAmount());

        });

        _categoryList.removeItemChangedListener((viewHolder, position) -> {
            TextView amnt  = viewHolder.itemView.findViewById(R.id.amount);
            amnt.setVisibility(View.GONE);
        });


        _addCategoryBtn.setOnClickListener(view12 -> {
            new FragmentHelper(_c).replace(new NewCategoryFragment(), " NewCategoyFragment", R.id.fragment_placeholder);

        });


        _addExpenseBtn.setOnClickListener(view1 -> new FragmentHelper(_c).replace(new NewFragment(), "NewExpenseFragment", R.id.fragment_placeholder));



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
                        new FragmentHelper(_c).replace(new MenuFragment(), " MenuFragment", R.id.fragment_placeholder);

                        return true;
                    }
                }
                return false;
            }
        });
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

    public void loadExpenseAdapter(int id) {
        List<ExpensesItem> expenses = _dbHandler.getExpItemsByCategory(id);
        _expensesList.setAdapter(new ExpensesAdapter(_c, expenses));
    }
}
