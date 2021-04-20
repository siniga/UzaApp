package com.agnet.uza.fragments.auth;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.BusinessType;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Business;
import com.agnet.uza.models.Address;
import com.agnet.uza.models.Success;
import com.agnet.uza.models.User;
import com.agnet.uza.service.Endpoint;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusinessRegistrationFragment extends Fragment {

    private FragmentActivity _c;
    private TextView _signinLink;
    private Toolbar _toolbar;
    private Button _registerBtn;
    private DatabaseHandler _dbHandler;
    private Gson _gson;
    private TextView _randomTxt;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private EditText _nameInput, _streetInput, _cityInput, _countryInput;
    private Spinner _businessTypesSpinner;
    private List<BusinessType> _businessType;
    private String _name, _street, _city, _country;
    private String _typeName;
    private int _typeId;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private String _TOKEN;
    private User _user;
    private int _USER_ID;
    private String TAG = "RESPONSE_TAG";

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_registration, container, false);
        _c = getActivity();

        //initializing
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);
        _gson = new Gson();

        _nameInput = view.findViewById(R.id.name_input);
        _streetInput = view.findViewById(R.id.street_input);
        _cityInput = view.findViewById(R.id.city_input);
        _countryInput = view.findViewById(R.id.country_input);
        _businessTypesSpinner = view.findViewById(R.id.business_types_spinner);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _registerBtn = view.findViewById(R.id.register_btn);

        _registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _name = _nameInput.getText().toString();
                _street = _streetInput.getText().toString();
                _city = _cityInput.getText().toString();
                _country = _countryInput.getText().toString();

                if (!checkEmptyFields()) {
                    if (checkConnection()) {
                        saveBusinessOnline();
                    } else {
                        saveBusinessTolocal();
                    }
                }

            }
        });

        _businessTypesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                _typeName = adapterView.getItemAtPosition(position).toString();
                _typeId = _businessType.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (_preferences.getString("USER_TOKEN", null) != null) {
            _TOKEN = _preferences.getString("USER_TOKEN", null);
        }

        if (_preferences.getInt("USER_ID", 0) != 0) {
            _USER_ID = _preferences.getInt("USER_ID", 0);
        }

        getCustomerType();

        return view;

    }

    private void getCustomerType() {

        _businessType = new ArrayList<>();
        _businessType.add(new BusinessType(1, "Urembo"));
        _businessType.add(new BusinessType(2, "Bar"));
        _businessType.add(new BusinessType(3, "Saluni ya kiume/kike"));
        _businessType.add(new BusinessType(4, "Mavazi"));
        _businessType.add(new BusinessType(5, "Usafiri"));
        _businessType.add(new BusinessType(6, "Furniture"));
        _businessType.add(new BusinessType(7, "Vyombo"));
        _businessType.add(new BusinessType(8, "Duka la mangi"));

        // Creating adapter for spinner
        ArrayAdapter<BusinessType> dataAdapter = new ArrayAdapter<BusinessType>(_c, android.R.layout.simple_spinner_item, _businessType);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        _businessTypesSpinner.setAdapter(dataAdapter);
    }

    private boolean checkEmptyFields() {
        if (_name.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la biashara!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_street.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la mtaa!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_city.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la mji!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_country.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la nchi!", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private void saveBusinessTolocal() {
        _dbHandler.createAddress(new Address(0, _street, _city, _country, 0));
        int lastId = _dbHandler.getLastId("addresses");

        _dbHandler.createBusiness(new Business(0, _name, lastId));
    }

    private void saveBusinessOnline() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("business");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Response res = _gson.fromJson(response, Response.class);

                    Log.d(TAG, response);

                    if (res.getCode() == 201) {
                        Success success = res.getSuccess();

                        Address address = success.getAddress();
                        Business business = success.getBusiness();

                        _dbHandler.createAddress(new Address(0, address.getName(), address.getCity(), address.getCountry(), 1));

                        int lastId = _dbHandler.getLastId("addresses");
                        _dbHandler.createBusiness(new Business(0, business.getName(), lastId));

                    } else {

                        _dbHandler.createAddress(new Address(0, _street, _city, _country, 0));

                        int lastId = _dbHandler.getLastId("addresses");
                        _dbHandler.createBusiness(new Business(0, _name, lastId));
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                    new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);



                },
                error -> {

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                    // Log.d("RegistrationFragment", "here" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    String errorMsg = "";
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        Log.i("log error", errorString);
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
                params.put("address", _street);
                params.put("city", _city);
                params.put("country", _country);
                params.put("typeId", "" + _typeId);
                params.put("userId", "" + _USER_ID);
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) _c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
