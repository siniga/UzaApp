package com.agnet.uza.fragments.staffs;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.fragments.stores.StoresFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.User;


public class EditStaffFragment extends Fragment implements View.OnClickListener{


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _name, _phone;
    private Button _updateStaff, _deleteStaff;
    private DatabaseHandler _dbHandler;
    private int _userId;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_staff, container, false);
        _c = getActivity();

         //initialize
        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _name = view.findViewById(R.id.name);
        _phone = view.findViewById(R.id.phone);
        _updateStaff = view.findViewById(R.id.update_staff_btn);
        _deleteStaff = view.findViewById(R.id.delete_staff_btn);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _updateStaff.setOnClickListener(this);
        _deleteStaff.setOnClickListener(this);

        _userId = _preferences.getInt("SELECTED_STAFF_ID", 0);
        User user = _dbHandler.getUser(_userId);

        _toolbar.setTitle("Edit "+user.getName() );

        _name.setText(user.getName());
        _phone.setText(user.getPhone());

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
                        new FragmentHelper(_c).replace(new StaffFragment(), "StaffFragment", R.id.fragment_placeholder);
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.update_staff_btn:

                if(_name.getText().toString().isEmpty() || _phone.getText().toString().isEmpty()){
                    Toast.makeText(_c, "Fileds should not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                _dbHandler.updateUser(new User(_userId,_phone.getText().toString(),_name.getText().toString()));
                new FragmentHelper(_c).replace(new StaffFragment(),"StaffFragment",R.id.fragment_placeholder);

                break;
            case R.id.delete_staff_btn:
                Toast.makeText(_c, "Item is deleted!", Toast.LENGTH_SHORT).show();

                _dbHandler.deleteUser(_userId);
                new FragmentHelper(_c).replace(new StaffFragment(), "StaffFragment", R.id.fragment_placeholder);
                break;
        }
    }


}
