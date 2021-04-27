package com.agnet.uza.fragments.staffs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CategoryAdapter;
import com.agnet.uza.adapters.StaffsAdapter;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.ReceiptFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Success;
import com.agnet.uza.models.User;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaffFragment extends Fragment   implements View.OnClickListener{

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _staffLayoutManager;
    private RecyclerView _staffList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private LinearLayout _newStaffBtn;
    private DatabaseHandler _dbHandler;
    private String _TOKEN;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staffs, container, false);
        _c = getActivity();

        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _staffList = view.findViewById(R.id.staff_list);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _newStaffBtn = view.findViewById(R.id.new_staff_btn);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _progressBar = view.findViewById(R.id.progress_bar);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _bottomNavigation.setVisibility(View.GONE);
        _toolbar.setTitle("Your Staffs");


        // list setup
        //category list
        _staffList.setHasFixedSize(true);
        _staffLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _staffList.setLayoutManager(_staffLayoutManager);

        _newStaffBtn.setOnClickListener(this);

        try {
            if (_preferences.getString("USER_TOKEN", null) != null) {
                _TOKEN = _preferences.getString("USER_TOKEN", null);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

        getLocalCategory();

        return view;

    }

    public void getLocalCategory() {

        List<User> users = _dbHandler.getUndeletedUsers();

        StaffsAdapter adapter = new StaffsAdapter(_c, users);
        _staffList.setAdapter(adapter);
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

    private void editUser() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("user/update");
        String url = Endpoint.getUrl();

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", response);
                        Response res = _gson.fromJson(response,Response.class);

                      /*  if (res.getCode() == 200) {
                            Success success = res.getSuccess();
                            User user = success.getUser();

                            _dbHandler.updateUser(new User(user.getId(), user.getPhone(), user.getName(), 0, user.getServerId(), 0));
                        }*/

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
        };

        mSingleton.getInstance(_c).addToRequestQueue(getRequest);
        //  postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_staff_btn:
                new FragmentHelper(_c).replace(new NewStaffFragment() ,"NewStaffFragmen", R.id.fragment_placeholder);
                break;
            case R.id.view_user_login:
//                new FragmentHelper(_c).replaceWithbackStack(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
    }
}
