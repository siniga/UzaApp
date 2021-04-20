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
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.BusinessType;
import com.agnet.uza.models.ResponseData;
import com.agnet.uza.models.Business;
import com.agnet.uza.models.Street;
import com.agnet.uza.service.Endpoint;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
    private  String _token;

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

        if(_preferences.getString("USER_TOKEN", null) != null){
            _token = _preferences.getString("USER_TOKEN", null);

            Log.d("HEREGETE",_token);
        }

        _registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _name = _nameInput.getText().toString();
                _street = _streetInput.getText().toString();
                _city = _cityInput.getText().toString();
                _country = _countryInput.getText().toString();

                registerBusinessOnline();
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

    /*private void saveUseTolocal() {
        _dbHandler.createUser(_phone, _name, 0);
    }*/

    private void registerBusinessOnline() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("business");
        String url = Endpoint.getUrl();
        Toast.makeText(_c, "" + _name + " " + _street + " " + _city + " " + _country + " " + _typeName + " " + _typeId, Toast.LENGTH_SHORT).show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ResponseData res = _gson.fromJson(response, ResponseData.class);

//                        Log.d("RESPONSE", _gson.toJson(business));

                        if (res.getCode() == 201) {

                            Business business = res.getBusiness();
                            _dbHandler.createStore(new Business(0,business.getName(), business.getStreetId()));

                        } else {

                            _dbHandler.createStreet(_street);

                            //get street last id and store it into business table to show its address
                            int lastId = _dbHandler.getLastId("streets");
                            _dbHandler.createStore(new Business(0,_name, lastId));
                        }
                        _progressBar.setVisibility(View.GONE);
                        _transparentLoader.setVisibility(View.GONE);


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+_token);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", _name);
                params.put("address", _street);
                params.put("city", "Kigali");
                params.put("country", _country);
                params.put("typeId", ""+_typeId);
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
