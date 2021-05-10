package com.agnet.uza.pages.checkout;


import android.annotation.SuppressLint;
import android.content.Context;
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

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.helpers.DatabaseHandler;
import com.google.gson.Gson;

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
        View view = inflater.inflate(R.layout.hm_receipt_fragment, container, false);

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


        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);


        return view;

    }


}



