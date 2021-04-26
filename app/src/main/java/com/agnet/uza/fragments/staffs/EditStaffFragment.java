package com.agnet.uza.fragments.staffs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.stores.StoresFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Success;
import com.agnet.uza.models.User;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;


public class EditStaffFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _nameInput, _phoneInput;
    private Button _updateStaff, _deleteStaff;
    private DatabaseHandler _dbHandler;
    private int _userId, _serverId;
    private Gson _gson;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private String _TOKEN;
    private String _name, _phone;
    ;


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
        _gson = new Gson();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _nameInput = view.findViewById(R.id.name);
        _phoneInput = view.findViewById(R.id.phone);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _progressBar = view.findViewById(R.id.progress_bar);
        _updateStaff = view.findViewById(R.id.update_staff_btn);
        _deleteStaff = view.findViewById(R.id.delete_staff_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _updateStaff.setOnClickListener(this);
        _deleteStaff.setOnClickListener(this);

        try {
            if (_preferences.getInt("SELECTED_STAFF_ID", 0) != 0) {
                _serverId = _preferences.getInt("USER_SERVER_ID", 0);
                _userId = _preferences.getInt("USER_ID", 0);
            }

            User user = _dbHandler.getUser(_userId);
            _toolbar.setTitle("Edit " + user.getName());

            _nameInput.setText(user.getName());
            _phoneInput.setText(user.getPhone());

            if (_preferences.getString("USER_TOKEN", null) != null) {
                _TOKEN = _preferences.getString("USER_TOKEN", null);
            }


        } catch (NullPointerException e) {
           e.getMessage();
        }


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
        switch (view.getId()) {
            case R.id.update_staff_btn:

                _name = _nameInput.getText().toString();
                _phone = _phoneInput.getText().toString();

                if (!validateFields()) {
                    editUser();
                }
                break;
            case R.id.delete_staff_btn:
                Toast.makeText(_c, "Item is deleted!", Toast.LENGTH_SHORT).show();

                _dbHandler.updateUser(new User(_userId, _phone, _name, 0, 0, 1));
                new FragmentHelper(_c).replace(new StaffFragment(), "StaffFragment", R.id.fragment_placeholder);
                break;
        }
    }

    private boolean validateFields() {
        if (_name.isEmpty() || _phone.isEmpty()) {
            Toast.makeText(_c, "Fileds should not be empty!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }


    private void editUser() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("user/update");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        Response res = _gson.fromJson(response, Response.class);

                        if (res.getCode() == 200) {
                            Success success = res.getSuccess();
                            User user = success.getUser();

                            _dbHandler.updateUser(new User(user.getId(), user.getPhone(), user.getName(), 0, user.getServerId(), 0));
                        }

                        _progressBar.setVisibility(View.GONE);
                        _transparentLoader.setVisibility(View.GONE);
                        new FragmentHelper(_c).replace(new StaffFragment(), "StaffFragment", R.id.fragment_placeholder);
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressBar.setVisibility(View.GONE);
                        _transparentLoader.setVisibility(View.GONE);

                         Log.d("RegistrationFragment", "here" + error.getMessage());
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                            Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + _TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", _name);
                params.put("phone", _phone);
                params.put("server_id", ""+_serverId);
                params.put("device_id", ""+_userId);

                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        //  postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}
