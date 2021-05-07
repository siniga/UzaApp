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

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.inventories.ManageCategoryFragment;
import com.agnet.uza.fragments.products.NewProductFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;


public class NewCategoryFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct;
    private ImageView _photoSelectorBtn;
    private ProductPhotoSelectorDialog _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private EditText _categoryName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_category, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _categoryName = view.findViewById(R.id.category_name);
        _saveProduct = view.findViewById(R.id.save_category_btn);

        _dbHandler = new DatabaseHandler(_c);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _saveProduct.setOnClickListener(this);

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
            case R.id.save_category_btn:
              /*  FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(new NewProductFragment());
                trans.commit();
                manager.popBackStack();*/
                if (_preferences.getInt("NEW_CATEGORY_FLAG", 0) == 0) {
                    if (!_categoryName.getText().toString().isEmpty()) {

                        _dbHandler.createCategory(new Category(0, _categoryName.getText().toString(), "", 0,0));

                    }

                    new FragmentHelper(_c).replace(new NewProductFragment(), "NewProductFragment", R.id.fragment_placeholder);

                } else {

                    if (!_categoryName.getText().toString().isEmpty()) {
                        _dbHandler.createCategory(new Category(0, _categoryName.getText().toString(), "", 0,0));
                    }
                 //   trans.remove(new InventoryFragment());
                    new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                }

                _editor.remove("NEW_CATEGORY_FLAG");
                _editor.commit();

                break;
        }
    }

}
