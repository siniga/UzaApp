package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.helpers.GenericTextWatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class LoginFragment extends Fragment {

    private FragmentActivity _c;
    private Toolbar _toolbar,_homeToolbar;
    private TextView _signupLink;
    private Button _loginBtn;
    private BottomNavigationView _btmNavBar;

    EditText _otpTextboxOne,  _otpTextboxTwo,  _otpTextboxThree,  _otpTextboxFour;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        _c = getActivity();

        //binding
        _signupLink = view.findViewById(R.id.signup_link);
        _loginBtn = view.findViewById(R.id.login_btn);
        _otpTextboxOne= view.findViewById(R.id.otp_edit_box1);
        _otpTextboxTwo = view.findViewById(R.id.otp_edit_box2);
        _otpTextboxThree = view.findViewById(R.id.otp_edit_box3);
        _otpTextboxFour = view.findViewById(R.id.otp_edit_box4);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _btmNavBar = _c.findViewById(R.id.bottom_navigation);

        _homeToolbar .setVisibility(View.GONE);
        _toolbar .setVisibility(View.GONE);
        _btmNavBar.setVisibility(View.GONE);

        EditText[] edit = {_otpTextboxOne, _otpTextboxTwo, _otpTextboxThree, _otpTextboxFour};

        _otpTextboxOne.addTextChangedListener(new GenericTextWatcher(_otpTextboxOne, edit));
        _otpTextboxTwo.addTextChangedListener(new GenericTextWatcher(_otpTextboxTwo, edit));
        _otpTextboxThree.addTextChangedListener(new GenericTextWatcher(_otpTextboxThree, edit));
        _otpTextboxFour.addTextChangedListener(new GenericTextWatcher(_otpTextboxFour, edit));


        //events
        _loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);


            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new RegistrationFragment(),"RegistrationFragment", R.id.fragment_placeholder);

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

}
