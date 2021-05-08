package com.agnet.uza.fragments.checkout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.dialogs.DialogCancelSale;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

public class ReceiptFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private TextView _errorMsg;
    private DatabaseHandler _dbHandler;
    private RecyclerView _productList;
    private Button _closeTabBtn, _closingTabLoader, _closedTabStatusBtn;
    private RecyclerView _tabHistoryList;
    private LinearLayoutManager _productLayoutManager, _tabHistoryLayoutManager;
    private RelativeLayout _currentTabStatus;
    private LinearLayout _currentTabLayout;
    private Toolbar _toolbar, _homeToolbar;
    private Button _continueBtn, _cancelBtn;
    private TextView _totalChange;
    private DecimalFormat _currencyformatter;
    private EditText _amountPaid;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receipt, container, false);

        //initialization
        _c = getActivity();
        _gson = new Gson();
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);
        _currencyformatter = new DecimalFormat("#,###,###");

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _continueBtn = view.findViewById(R.id.continue_btn);
        _totalChange = view.findViewById(R.id.total_change);

        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        BottomNavigationView navigationView = _c.findViewById(R.id.bottom_navigation);
        navigationView.setVisibility(View.GONE);

        try {
            String totalChange = _preferences.getString("TOTAL_CHANGE", null);
         //   String currencyChange = _currencyformatter.format(totalChange);
            _totalChange.setText("TZS:"+totalChange);
        } catch (NullPointerException e) {
            e.getMessage();
        }

        _continueBtn.setOnClickListener(view1 -> {
            _dbHandler.deleteCart();
            new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
        });

        return view;

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

    public void launchCancelSaleDialog() {
        DialogCancelSale dialog = new DialogCancelSale(_c);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
            }
        });
    }
}



