package com.agnet.uza.pages.stores;

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
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Address;
import com.agnet.uza.models.Business;
import com.agnet.uza.models.BusinessType;
import com.agnet.uza.models.Country;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Success;
import com.agnet.uza.service.Endpoint;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewStoreFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;

    private Button _saveStore;
    private DatabaseHandler _dbHandler;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private Gson _gson;
    private String _name, _address;
    private EditText _nameInput, _addressInput;
    private Spinner _businessTypesSpinner;
    private List<BusinessType> _businessType;
    private String _TOKEN;
    private Spinner _citySpinner;
    private List<Country> _countryList, _cityList;
    private AutoCompleteTextView _countryAutocomplete;
    private String _COUNTRY_TOKEN;
    private String _selectedCountry,_selectedCity;
    private EditText _cityInput;
    private LinearLayout _citySpinnerWrapper;
    private int _userId;
    private String _typeName;
    private int _typeId;
    private int SYNC_STATUS_ON = 1;
    private int SYNC_STATUS_OFF = 0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_fragment_new_store, container, false);
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

        _businessTypesSpinner = view.findViewById(R.id.business_types_spinner);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);

        _citySpinner = view.findViewById(R.id.city_spinner);
        _countryAutocomplete = view.findViewById(R.id.country_autocomplete);
        _cityInput  = view.findViewById(R.id.city_input);
        _citySpinnerWrapper =view.findViewById(R.id.city_spinner_wrapper);
        _saveStore = view.findViewById(R.id.save_store_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        //events
        _saveStore.setOnClickListener(this);
        _saveStore.setClickable(true);

        if (_preferences.getString("USER_TOKEN", null) != null) {
            _TOKEN = _preferences.getString("USER_TOKEN", null);
//            Log.d("TOKEN_HERE",""+_TOKEN);
        }

        if (_preferences.getInt("USER_ID", 0) != 0) {
            _userId = _preferences.getInt("USER_ID", 0);
          //  int serveid = _preferences.getInt("USER_SERVER_ID", 0);

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

        _citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                _selectedCity = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        _countryAutocomplete.setOnItemClickListener((parent, view1, position, l) -> {
            // fetch the user selected value
            _selectedCountry = parent.getItemAtPosition(position).toString();

            getCities();

        });

        getCustomerType();
        getCountryToken();

        _cityList = new ArrayList<>();
        _cityList.add(new Country(0,"Chagua mji"));

        // Creating adapter for spinner
        ArrayAdapter<Country> dataAdapter = new ArrayAdapter<Country>(_c, android.R.layout.simple_spinner_item, _cityList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        _citySpinner.setAdapter(dataAdapter);

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
            case R.id.save_store_btn:

                _name = _nameInput.getText().toString();
                _address = _addressInput.getText().toString();

                if (!checkEmptyFields()) {
                    _saveStore.setClickable(false);
                    saveBusiness();
                }else {
                    _saveStore.setClickable(true);
                }



                break;
        }
    }

    private void saveBusiness() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("business");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("RESPONSE", response);
                    Response res = _gson.fromJson(response, Response.class);

                    if (res.getCode() == 201) {
                        Success success = res.getSuccess();

                        Address address = success.getAddress();
                        Business business = success.getBusiness();

                        _dbHandler.createAddress(new Address(address.getId(), address.getName(), address.getCity(), address.getCountry(), address.getId()));

                        int lastId = _dbHandler.getLastId("addresses");
                        _dbHandler.createBusiness(new Business(business.getId(), business.getName(), lastId, business.getId()));

                        _editor.putInt("BUSINESS_ID", business.getId());
                        _editor.commit();

                    } else {

                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveStore.setClickable(true);
                    new FragmentHelper(_c).replace(new StoresFragment(), "StoresFragment", R.id.fragment_placeholder);

                },
                error -> {
                    _saveStore.setClickable(true);
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                     Log.d("RESPONSE_ERROR", "here" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
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
                params.put("city", _selectedCity);
                params.put("country", _selectedCountry);
                params.put("typeId", "" + _typeId);
                params.put("userId", "" + _userId);
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        // postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private boolean checkEmptyFields() {
        if (_name.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la biashara!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_address.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina la mtaa!", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private void getCountryToken() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        String tokenUrl = "https://www.universal-tutorial.com/api/getaccesstoken";

        StringRequest postRequest = new StringRequest(Request.Method.GET, tokenUrl,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        _COUNTRY_TOKEN = jsonObject.getString("auth_token");
                        getCountries();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                  //  Log.d(TAG, response);


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
                params.put("Accept", "application/json");
                params.put("api-token", "73mD2LeNtk6n1PsvuvXT1bx0Ot6Opze-ZkqZpc3sA0p7t3Bu3JjljyVM6WjrfSuzNlM");
                params.put("user-email","peterkhamis5@gmail.com");
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void  getCities(){

        String url = "https://www.universal-tutorial.com/api/states/"+_selectedCountry;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    _cityList.clear();
                    String res = _gson.toJson(response);
                    try {
                        JSONArray cityArr = new JSONArray(response);
                        for (int i = 0; i<= cityArr.length()-1; i++){
                            JSONObject city = cityArr.getJSONObject(i);
                            _cityList.add(new Country(0,city.getString("state_name")));
                        }

                        // Creating adapter for spinner
                        ArrayAdapter<Country> dataAdapter = new ArrayAdapter<>(_c, android.R.layout.simple_spinner_item, _cityList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        _citySpinner.setAdapter(dataAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("error", e.toString());
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                },
                error -> {

                    // Log.d("RegistrationFragment", "here" + error.getMessage());
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
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+ _COUNTRY_TOKEN);

                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);

    }

    private void getCountries() {
        String url = "https://www.universal-tutorial.com/api/countries";

        _countryList = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    //  String res = _gson.toJson(response);
                    try {
                        JSONArray countryArr = new JSONArray(response);
                        for (int i = 0; i<= countryArr.length()-1; i++){
                            JSONObject country = countryArr.getJSONObject(i);
                            _countryList.add(new Country(0,country.getString("country_name")));
                        }


                        // Creating adapter for spinner
                        ArrayAdapter<Country> dataAdapter = new ArrayAdapter<>(_c, android.R.layout.simple_spinner_item, _countryList);

                        // Drop down layout style - list view with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        // attaching data adapter to spinner
                        _countryAutocomplete.setAdapter(dataAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("error", e.toString());
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

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
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer "+ _COUNTRY_TOKEN);

                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
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


}
