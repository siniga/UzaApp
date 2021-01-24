package com.agnet.uza.activities;

import android.os.Bundle;

import com.agnet.uza.adapters.ProductAdapter;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.fragments.auth.LoginFragment;
import com.agnet.uza.fragments.MenuFragment;
import com.agnet.uza.fragments.ReportFragment;
import com.agnet.uza.fragments.TransactionFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.agnet.uza.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomSheetBehavior _sheetBehavior;
    private BottomSheetBehavior _cartSheetBehavior;
    private CoordinatorLayout _coordinator;
    private RelativeLayout _btmSheet, _btmSheetCart;
    private LinearLayoutManager _categoryLayoutManager, _cartListManager;
    private RecyclerView _categoryList,_cartList;
    private BottomNavigationView _btmNavBar;
    private List<Product> _products;;
    private ProductAdapter _productAdapter;
    private DatabaseHandler _dbHandler;
    private LinearLayout _toolbarIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //binding
        _btmNavBar = findViewById(R.id.bottom_navigation);
        _toolbarIcon = findViewById(R.id.toolbar_icon);

        //initialize
        _btmNavBar.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        _dbHandler = new DatabaseHandler(this);

        Log.d("HEHHRHH", ""+_dbHandler.isTableEmpty("users"));
        //check if user is logged in

        //events
        _toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(MainActivity.this).replaceWithbackStack(new MenuFragment(), " MenuFragment", R.id.fragment_placeholder);

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

       LogUser();


    }

    private void LogUser() {
        if(!_dbHandler.isTableEmpty("users")){

            if(_dbHandler.getUserPhone().isEmpty()){
                new FragmentHelper(MainActivity.this).replace(new LoginFragment(), "LoginFragment", R.id.fragment_placeholder);

            }else {
                new FragmentHelper(MainActivity.this).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);

            }
        }else {

            new FragmentHelper(MainActivity.this).replace(new LoginFragment(), "LoginFragment", R.id.fragment_placeholder);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setHomeIconBottomNav() {
        _btmNavBar.getMenu().getItem(0).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener

            = item -> {

        boolean mState = !item.isChecked();
        switch (item.getItemId()) {
            case R.id.action_home:

                item.setChecked(mState);
                new FragmentHelper(MainActivity.this).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            case R.id.action_report:

                item.setChecked(mState);
                new FragmentHelper(MainActivity.this).replaceWithbackStack(new ReportFragment(), "ReportFragment", R.id.fragment_placeholder);
                break;
            case R.id.action_transaction:
                item.setChecked(mState);
                new FragmentHelper(MainActivity.this).replaceWithbackStack(new TransactionFragment(), "TransactionFragment", R.id.fragment_placeholder);

                break;
        }
        return false;
    };


}
