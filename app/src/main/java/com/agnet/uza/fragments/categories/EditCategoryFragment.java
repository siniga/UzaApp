package com.agnet.uza.fragments.categories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.products.NewProductFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;


public class EditCategoryFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _updateCategory, _deleteCategory;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private EditText _categoryName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _categoryName = view.findViewById(R.id.category_name);
        _updateCategory = view.findViewById(R.id.update_category_btn);
        _deleteCategory = view.findViewById(R.id.delete_category_btn);

        _categoryName.setText(_preferences.getString("SELECTED_CATEGORY_NAME", null));

        _dbHandler = new DatabaseHandler(_c);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _updateCategory.setOnClickListener(this);
        _deleteCategory.setOnClickListener(this);

        //set page flag so that when user go back to inventory fragment
        //they can be redirected to category tab
        _editor.putInt("INVENTORY_PAGE_TYPE", 1);
        _editor.commit();

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                        return true;
                    }
                }
                return false;
            }
        });
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

                if (!_categoryName.getText().toString().isEmpty()) {
                    _dbHandler.updateCategory(new Category(_preferences.getInt("SELECTED_CATEGORY_ID", 0),
                            _categoryName.getText().toString(), "", 0,0));
                    new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                } else {
                    Toast.makeText(_c, "Field should not be empty!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.delete_category_btn:

                _dbHandler.deleteCategory(_preferences.getInt("SELECTED_CATEGORY_ID", 0));
                new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                break;
        }
    }

}
