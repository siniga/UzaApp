package com.agnet.uza.pages.auth;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.helpers.GenericTextWatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


public class CreatePinFragment extends Fragment {

    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private BottomNavigationView _btmNavBar;
    private Button _createPinBtn;
    private EditText _otpTextboxOne, _otpTextboxTwo, _otpTextboxThree, _otpTextboxFour;
    private DatabaseHandler _dbHandler;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_pin, container, false);
        _c = getActivity();

        //initializing
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _createPinBtn = view.findViewById(R.id.create_pin_btn);
        _otpTextboxOne = view.findViewById(R.id.otp_edit_box1);
        _otpTextboxTwo = view.findViewById(R.id.otp_edit_box2);
        _otpTextboxThree = view.findViewById(R.id.otp_edit_box3);
        _otpTextboxFour = view.findViewById(R.id.otp_edit_box4);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _btmNavBar = _c.findViewById(R.id.bottom_navigation);

        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.GONE);
        _btmNavBar.setVisibility(View.GONE);

        EditText[] edit = {_otpTextboxOne, _otpTextboxTwo, _otpTextboxThree, _otpTextboxFour};

        _otpTextboxOne.addTextChangedListener(new GenericTextWatcher(_otpTextboxOne, edit));
        _otpTextboxTwo.addTextChangedListener(new GenericTextWatcher(_otpTextboxTwo, edit));
        _otpTextboxThree.addTextChangedListener(new GenericTextWatcher(_otpTextboxThree, edit));
        _otpTextboxFour.addTextChangedListener(new GenericTextWatcher(_otpTextboxFour, edit));


        //events
        _createPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin1 = _otpTextboxOne.getText().toString();
                String pin2 = _otpTextboxTwo.getText().toString();
                String pin3 = _otpTextboxThree.getText().toString();
                String pin4 = _otpTextboxFour.getText().toString();

                if (pin1.isEmpty()) {
                    Toast.makeText(_c, "Pin must be four digits!", Toast.LENGTH_SHORT).show();
                } else if (pin2.isEmpty()) {
                    Toast.makeText(_c, "Pin must be four digits!", Toast.LENGTH_SHORT).show();
                } else if (pin3.isEmpty()) {
                    Toast.makeText(_c, "Pin must be four digits!", Toast.LENGTH_SHORT).show();
                } else if (pin4.isEmpty()) {
                    Toast.makeText(_c, "Pin must be four digits!", Toast.LENGTH_SHORT).show();
                } else {

                    String pinNum = pin1 + pin2 + pin3 + pin4;

                    _dbHandler.updateUserPin(Integer.parseInt(pinNum));
                    removePaymentFragmentFrmBackstack();
                    new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                }


            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void removePaymentFragmentFrmBackstack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

}
