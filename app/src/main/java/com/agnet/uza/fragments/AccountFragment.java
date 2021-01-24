package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.helpers.DatabaseHandler;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class AccountFragment extends Fragment {

    private FragmentActivity _c;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private NotificationManagerCompat _notificationManager;
    private Button _submitUserDetailsBtn;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        _c = getActivity();

        final EditText phoneNum = view.findViewById(R.id.phone_input);
        final EditText outletName = view.findViewById(R.id.outlet_name_input);
        final TextView registerError = view.findViewById(R.id.register_name_error);

        _dbHandler = new DatabaseHandler(_c);

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        if (!_dbHandler.getUserPhone().isEmpty()) {
            phoneNum.setText(_dbHandler.getUserPhone());

            if (_dbHandler.getUserName().equals("anonymous")) {

                outletName.setHint("Sajili jina la duka");
                registerError.setText("Sajili jina la duka ili upate huduma bora zaidi!");

            } else {

                outletName.setText(_dbHandler.getUserName());
                registerError.setText("");
            }

        }

        _submitUserDetailsBtn = view.findViewById(R.id.submit_user_details);
        _submitUserDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!phoneNum.getText().toString().isEmpty()) {

                    //create user if not created
                  //  _dbHandler.createUser(phoneNum.getText().toString(), outletName.getText().toString());


                } else {
                    Toast.makeText(_c, "Weka namba ya simu ili kuendelea!", Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;

    }

}
