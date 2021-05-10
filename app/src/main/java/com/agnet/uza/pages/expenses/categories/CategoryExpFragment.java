package com.agnet.uza.pages.expenses.categories;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.DateHelper;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CategoryExpFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _expensesLayoutManager;
    private RecyclerView _expensesList;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newBtn;
    private DatabaseHandler _dbHandler;
    private TextView _totalCatExpenses;
    private BottomSheetBehavior _behavior;
    private CoordinatorLayout _coordinatorLayout;
    private Button _discardBtn, _viewCategory, _editCategory;
    private LinearLayout _errorMsg;
    private TextView _currentDate;
    private List<ExpensesCategory> _categories;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_fragment_category, container, false);
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
        _errorMsg = view.findViewById(R.id.error_msg);
        _currentDate = view.findViewById(R.id.date);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);
        _toolbar.setTitle("Expenses");
        _currentDate.setText(date);

        _newBtn.setOnClickListener(view1 -> new FragmentHelper(_c).replaceWithbackStack(new NewExpCategoryFragment(), "NewExpenseCategoryFragment", R.id.fragment_placeholder));

        //display total category expenses amount
    /*    if(_dbHandler.getExpCategoryTotalAmt() !=  null){

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
*/
        // list setup
        _expensesList.setHasFixedSize(true);
        _expensesLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _expensesList.setLayoutManager(_expensesLayoutManager);

        List<ExpensesCategory> categories = _dbHandler.getExpensesCategories();
        CategoryExpAdapter adapter = new CategoryExpAdapter(_c, categories, this);
        _expensesList.setAdapter(adapter);

        return view;

    }


    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continue_btn:
                break;
            default:
                break;
        }
    }
}
