package com.agnet.uza.pages.expenses.expenses;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.agnet.uza.pages.expenses.categories.FilterExpCategoryAdapter;
import com.agnet.uza.adapters.expenses.expenses.ExpensesAdapter;
import com.agnet.uza.pages.MenuFragment;
import com.agnet.uza.pages.expenses.categories.CategoryExpFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;
import com.agnet.uza.models.ExpensesItem;
import com.agnet.uza.pages.expenses.categories.NewExpCategoryFragment;
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
    private LinearLayout _addCategoryBtn;
    private DatabaseHandler _dbHandler;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DiscreteScrollView _categoryList;
    private DecimalFormat _currencyformatter;
    private TextView _amnt, _categoryExpHdrText;
    private ImageButton _addExpenseBtn;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_fragment_expenses, container, false);
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
        _categoryExpHdrText = view.findViewById(R.id.category_exp_hder);
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

        List<ExpensesCategory> categories = _dbHandler.getExpensesCategories();
        FilterExpCategoryAdapter adapter = new FilterExpCategoryAdapter(_c, categories, this);
        _categoryList.setAdapter(adapter);

        if (_preferences.getInt("EXP_CATEGORY_POSITION", 0) != 0) {

            _categoryList.scrollToPosition(_preferences.getInt("EXP_CATEGORY_POSITION", 0));
        } else {
            _categoryList.scrollToPosition(1);

        }
        _categoryList.setItemTransformer((item, position) -> {

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
            _amnt = viewHolder.itemView.findViewById(R.id.amount);

            _editor.putInt("EXP_CATEGORY_ID", currentCategory.getId());
            _editor.putString("EXP_CATEGORY_NAME", currentCategory.getName());
            _editor.putInt("EXP_CATEGORY_POSITION", position);
            _editor.commit();

            _categoryExpHdrText.setText(currentCategory.getName());

            try{
                String categoryTotalAmt = _dbHandler.getExpensesTotalAmtByCategory(currentCategory.getId());
                if(!categoryTotalAmt.isEmpty()){
                    _amnt.setText(categoryTotalAmt);
                }
            }catch (NullPointerException e) {

            }

        });

        _categoryList.removeItemChangedListener((viewHolder, position) -> {
            //_amnt.setVisibility(View.GONE);

        });


        _addCategoryBtn.setOnClickListener(view12 -> {
            new FragmentHelper(_c).replace(new NewExpCategoryFragment(), "NewExpCategoryFragment", R.id.fragment_placeholder);

        });


        _addExpenseBtn.setOnClickListener(view1 ->
                new FragmentHelper(_c).replace(new NewExpFragment(), "NewExpenseFragment", R.id.fragment_placeholder)
        );


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
                        _editor.remove("EXP_CATEGORY_POSITION");
                        _editor.remove("EXP_CATEGORY_NAME");
                        _editor.commit();

                        new FragmentHelper(_c).replace(new MenuFragment(), " MenuFragment", R.id.fragment_placeholder);

                        return true;
                    }
                }
                return false;
            }
        });
    }


    public void loadExpenseAdapter(int id) {
        List<ExpensesItem> expenses = _dbHandler.getExpItemsByCategory(id);
        _expensesList.setAdapter(new ExpensesAdapter(_c, expenses));
    }

}
