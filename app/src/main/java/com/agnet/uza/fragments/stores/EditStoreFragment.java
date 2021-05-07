package com.agnet.uza.fragments.stores;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.fragments.staffs.StaffFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Address;
import com.agnet.uza.models.Business;
import com.agnet.uza.models.BusinessType;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Success;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditStoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _nameInput, _addressInput;
    private Button _saveStore, _deleteStore;
    private DatabaseHandler _dbHandler;
    private int _selectedBusinessId;
    private Spinner _businessTypesSpinner;
    private List<BusinessType> _businessType;
    private int _typeId;
    private String _typeName, _name, _address, _city, _country;
    private AutoCompleteTextView _countryAutocomplete;
    private EditText _cityInput;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private Gson _gson;
    private String _TOKEN;
    private int _serverId;
    private Business _business;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_store, container, false);
        _c = getActivity();

        //initialize
        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _gson = new Gson();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _nameInput = view.findViewById(R.id.name_input);
        _addressInput = view.findViewById(R.id.street_input);
        _countryAutocomplete = view.findViewById(R.id.country_autocomplete);
        _businessTypesSpinner = view.findViewById(R.id.business_types_spinner);
        _cityInput = view.findViewById(R.id.city_input);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);

        _saveStore = view.findViewById(R.id.update_store_btn);
        _deleteStore = view.findViewById(R.id.delete_store_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        try {
            if (_preferences.getInt("SELECTED_STORE_ID", 0) != 0) {
                _selectedBusinessId = _preferences.getInt("SELECTED_STORE_ID", 0);
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }

        _business = _dbHandler.getSelectedBusiness(_selectedBusinessId);
        _toolbar.setTitle("Edit " + _business.getName());
        _nameInput.setText(_business.getName());
        _addressInput.setText(_business.getAddress().getName());
        _countryAutocomplete.setText(_business.getAddress().getCountry());
        _cityInput.setText(_business.getAddress().getCity());

        //  Log.d("SELECTEID", ""+_gson.toJson(_business));
        //events
        _saveStore.setOnClickListener(this);
        _deleteStore.setOnClickListener(this);

        if (_preferences.getString("USER_TOKEN", null) != null) {
            _TOKEN = _preferences.getString("USER_TOKEN", null);
        }

        if (_preferences.getInt("USER_SERVER_ID", 0) != 0) {
            _serverId = _preferences.getInt("USER_SERVER_ID", 0);
        }

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

        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_store_btn:
                _name = _nameInput.getText().toString();
                _address = _addressInput.getText().toString();
                _country = _countryAutocomplete.getText().toString();
                _city = _cityInput.getText().toString();

                if (!checkEmptyFields()) {
                    _saveStore.setClickable(true);
                    saveBusiness();
                } else {
                    _saveStore.setClickable(false);
                }
                break;
            case R.id.delete_store_btn:
                deleteBusiness();
                break;
        }
    }

    private boolean checkEmptyFields() {
        if (_name.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la biashara!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_address.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la mtaa!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_city.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la mji!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_country.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la nchi", Toast.LENGTH_LONG).show();
            return true;
        }


        return false;
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

    private void saveBusiness() {

        //Log.d("RESPONSE", "" + _business.getServerId());
        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("business/update");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Response res = _gson.fromJson(response, Response.class);
                    if (res.getCode() == 200) {
                        Success success = res.getSuccess();

                        Address address = success.getAddress();
                        Business business = success.getBusiness();

                        _dbHandler.updateAddress(new Address(_business.getAddress().getId(), address.getName(), address.getCity(), address.getCountry(), address.getServerId()));
                        _dbHandler.updateBusiness(new Business(_business.getId(), business.getName(), _business.getAddress().getId(), business.getServerId()));

                    } else {

                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }


                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveStore.setClickable(true);
                    new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);

                },
                error -> {

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveStore.setClickable(true);

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
                params.put("address", _address);
                params.put("city", _city);
                params.put("country", _country);
                params.put("typeId", "" + _typeId);
                params.put("id", "" + _business.getServerId());
                params.put("addressId", "" + _business.getAddress().getServerId());
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        // postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void deleteBusiness() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("business/device/delete/" + _business.getServerId());
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d("RESPONSE", ""+_userId);

                        _progressBar.setVisibility(View.GONE);
                        _transparentLoader.setVisibility(View.GONE);

                        _dbHandler.deleteBusiness(_business.getId());

                        new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);
                    }

                },
                error -> {
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveStore.setClickable(true);

                    Log.d("RegistrationFragment", "here" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    String errorMsg = "";
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        Log.i("log error", errorString);
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
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

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        //  postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}
