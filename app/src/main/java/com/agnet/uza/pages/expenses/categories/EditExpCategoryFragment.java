package com.agnet.uza.pages.expenses.categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.pages.expenses.expenses.ExpensesFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.ExpensesCategory;


public class EditExpCategoryFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _updateCategory, _deleteCategory;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private EditText _categoryName;
    private int _expCategoryId;
    private String _expCategoryName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exp_fragment_edit_category, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);


        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _categoryName = view.findViewById(R.id.category_name);
        _updateCategory = view.findViewById(R.id.update_category_btn);
        _deleteCategory = view.findViewById(R.id.delete_category_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _updateCategory.setOnClickListener(this);
        _deleteCategory.setOnClickListener(this);

        if( !_preferences.getString("EXPCATEGORY_NAME", null).isEmpty()){
            _categoryName.setText(_preferences.getString("EXPCATEGORY_NAME", null));
           _expCategoryName =   _preferences.getString("EXPCATEGORY_NAME", null);
        }

        if(_preferences.getInt("EXPCATEGORY_ID", 0) != 0){
           _expCategoryId = _preferences.getInt("EXPCATEGORY_ID", 0);
        }


        _toolbar.setTitle("Edit "+_expCategoryName );



        return view;

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_category_btn:
              /*  FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new NewProductFragment());
                trans.commit();
                manager.popBackStack();*/

                String name = _categoryName.getText().toString();

                if (!_categoryName.getText().toString().isEmpty()) {

                    _dbHandler.updateExpenseCategory(new ExpensesCategory(_expCategoryId,name,""));
                    new FragmentHelper(_c).replaceWithbackStack(new ExpensesFragment(), "ExpensesFragment", R.id.fragment_placeholder);

                } else {
                    Toast.makeText(_c, "Field should not be empty!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.delete_category_btn:

                _dbHandler.deleteExpenseCategory(_expCategoryId);
                _dbHandler.deleteExpenseItemByCategory(_expCategoryId);

                new FragmentHelper(_c).replace(new CategoryExpFragment(), "ExpensesCategoryFragment", R.id.fragment_placeholder);
                break;
        }
    }

}
