package com.agnet.uza.fragments.expenses.categories;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.ExpensesCategoryAdapter;
import com.agnet.uza.fragments.MenuFragment;
import com.agnet.uza.fragments.checkout.PaymentFragment;
import com.agnet.uza.fragments.expenses.expenses.ExpensesFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.DateHelper;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

public class CategoryFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _expensesLayoutManager;
    private RecyclerView _expensesList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newBtn;
    private DatabaseHandler _dbHandler;
    private TextView _totalCatExpenses;
    private BottomSheetBehavior _behavior;
    private CoordinatorLayout _coordinatorLayout;
    private Button _discardBtn, _viewCategory, _editCategory;
    private LinearLayout _errorMsg;
    private TextView _currentDate;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses_category, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        _gson = new Gson();

        String date = DateHelper.getMonth();

        //binding
        _expensesList = view.findViewById(R.id.expenses_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _totalCatExpenses = view.findViewById(R.id.total_category_expenses);
        _newBtn = view.findViewById(R.id.new_expense_category_btn);
        _coordinatorLayout = view.findViewById(R.id.btmsheet_coodnate_layout);
        _errorMsg = view.findViewById(R.id.error_msg);
        _currentDate = view.findViewById(R.id.date);

        View bottomSheet = view.findViewById(R.id.category_expense_btmsheet);
        _behavior = BottomSheetBehavior.from(bottomSheet);
        _discardBtn = bottomSheet.findViewById(R.id.discard);
        _viewCategory = view.findViewById(R.id.view_category);
        _editCategory = view.findViewById(R.id.edit_category);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);
        _behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        _toolbar.setTitle("Expenses");
        _currentDate.setText(date);


        //events
        _behavior = BottomSheetBehavior.from(bottomSheet);
        _behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        _newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new NewCategoryFragment(), "NewExpenseCategoryFragment", R.id.fragment_placeholder);
            }
        });

        _viewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new ExpensesFragment(),"ExpensesFragment", R.id.fragment_placeholder);
            }
        });

        _editCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new EditCategoryFragment(),"EditExpenseCategoryFragment", R.id.fragment_placeholder);

            }
        });


        _discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBtmBehavior();
            }
        });

        //display total category expenses amount
        if(_dbHandler.getExpCategoryTotalAmt() !=  null){

            int formatedPric1e = Integer.parseInt(_dbHandler.getExpCategoryTotalAmt());

            Log.d("ERRPRPRPR", ""+formatedPric1e);

            try{
                int formatedPrice = Integer.parseInt(_dbHandler.getExpCategoryTotalAmt());

                Log.d("ERRPRPRPR", ""+formatedPrice);
                _totalCatExpenses.setText(" "+formatter.format(formatedPrice));

            }catch (NumberFormatException exception){
                _totalCatExpenses.setText("");
                Log.e("log_error",exception.toString());
            }

        }else {
            _totalCatExpenses.setText("0.0");
        }

        // list setup
        _expensesList.setHasFixedSize(true);
        _expensesLayoutManager= new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _expensesList.setLayoutManager( _expensesLayoutManager );

        List<ExpensesCategory> expenses = _dbHandler.getExpensesCategories();
        ExpensesCategoryAdapter adapter = new ExpensesCategoryAdapter(_c, expenses, this);
        _expensesList.setAdapter(adapter);

        return view;

    }

    public void showBtmBehavior(){
        _behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void hideBtmBehavior(){
        _behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

                        new FragmentHelper(_c).replace(new MenuFragment(), "MenuFragment", R.id.fragment_placeholder);

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
        hideBtmBehavior();

        List<ExpensesCategory> categories = _dbHandler.getExpensesCategories();
        if (categories.size() == 0){
           _expensesList.setVisibility(View.GONE);
            _errorMsg.setVisibility(View.VISIBLE);
        }

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
            case R.id.view_user_login:
//                new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
