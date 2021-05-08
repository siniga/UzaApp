package com.agnet.uza.fragments.checkout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.cottacush.android.currencyedittext.CurrencyEditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;

public class PaymentFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;
    private Gson _gson;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private Toolbar _toolbar, _homeToolbar;
    private TextView _newSaleBtn;
    private DatabaseHandler _dbHandler;
    private CurrencyEditText _amountPaid;
    private Button _continueBtn;
    private DecimalFormat _currencyformatter;
    private double _totalAmount;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

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
        _newSaleBtn = view.findViewById(R.id.new_sale_btn);
        _amountPaid = view.findViewById(R.id.customer_paid);
        _continueBtn = view.findViewById(R.id.continue_btn);

        try {

            _totalAmount = _dbHandler.getCartTotalAmt();
           String currencyAmnt = _currencyformatter.format(_totalAmount);
            _amountPaid.setHint("" + currencyAmnt);

        } catch (NullPointerException e) {

        }

        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _continueBtn.setOnClickListener(this);

        BottomNavigationView navigationView = _c.findViewById(R.id.bottom_navigation);
        navigationView.setVisibility(View.GONE);

        showKeyboard(_amountPaid);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_sale_btn:
                //create order
                updateSaleStatus();
                new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                removePaymentFragmentFrmBackstack();
                break;
            case R.id.view_user_login:
//                new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            case R.id.continue_btn:
                calculateTotalChange();
                break;
            default:
                break;
        }
    }


    public void calculateTotalChange() {

        try {
            double amountPaid;
            if (!_amountPaid.getText().toString().isEmpty()) {
                amountPaid = _amountPaid.getNumericValue();
            } else {
                amountPaid = Double.valueOf(_totalAmount);

            }



            if(_totalAmount > amountPaid){
                Toast.makeText(_c, "Pesa ulioingiza ni ndogo kuliko unayodai", Toast.LENGTH_LONG).show();
            }else {
                double totalChange = amountPaid - _totalAmount;
                _editor.putString("TOTAL_CHANGE", "" + totalChange);
                _editor.commit();

                new FragmentHelper(_c).replaceWithbackStack(new ReceiptFragment(), "ReceiptFragment", R.id.fragment_placeholder);
            }




        } catch (NullPointerException e) {
           e.getMessage();
        }

    }

    private void updateSaleStatus() {
        if (_dbHandler.isValueExist(0, "orders", "status")) {
            //  Toast.makeText(_c, "juju", Toast.LENGTH_SHORT).show();
            _dbHandler.updateOrder(1);

        }
    }

    private void removePaymentFragmentFrmBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public static void showKeyboard(EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

}
