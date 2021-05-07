package com.agnet.uza.fragments.auth;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.helpers.AndroidDatabaseManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Success;
import com.agnet.uza.models.User;
import com.agnet.uza.service.Endpoint;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;


public class RegistrationFragment extends Fragment {

    private FragmentActivity _c;
    private TextView _signinLink;
    private Toolbar _toolbar;
    private Button _registerBtn;
    private DatabaseHandler _dbHandler;
    private EditText _phoneInputEdTxt, _nameInputEdTxt, _passwordInput;
    private String _name, _phone, _password;
    private Gson _gson;
    private TextView _randomTxt;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private String TAG = "RESPONSE_TAG";

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        _c = getActivity();

        //initializing
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);
        _gson = new Gson();

        //binding
        _signinLink = view.findViewById(R.id.signin_link);
        _registerBtn = view.findViewById(R.id.register_btn);
        _phoneInputEdTxt = view.findViewById(R.id.phone_input);
        _nameInputEdTxt = view.findViewById(R.id.name_input);
        _passwordInput = view.findViewById(R.id.password_input);
        _randomTxt = view.findViewById(R.id.random_txt);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);

        //set and get
        TextView viewdb = view.findViewById(R.id.welcom_hdr);
        viewdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_c, AndroidDatabaseManager.class);
                _c.startActivity(intent);
            }
        });

        //events
        _signinLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FragmentHelper(_c).replace(new LoginFragment(), "LoginFragment", R.id.fragment_placeholder);

            }
        });


        _registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _phone = _phoneInputEdTxt.getText().toString();
                _name = _nameInputEdTxt.getText().toString();
                _password = _passwordInput.getText().toString();
                // new FragmentHelper(_c).replace(new BusinessRegistrationFragment(), "BusinessRegistrationFragment", R.id.fragment_placeholder);

                if (!checkEmptyFields()) {
                    if (checkConnection()) {
                        _registerBtn.setClickable(false);
                        saveUserToServer();
                    } else {
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        _randomTxt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Intent intent = new Intent(_c, AndroidDatabaseManager.class);
                _c.startActivity(intent);

                return false;
            }
        });

        return view;

    }

    private boolean checkEmptyFields() {

        if (_name.isEmpty()) {
            Toast.makeText(_c, "Ingiza jina!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_phone.isEmpty() || _phone.length() < 10) {

            Toast.makeText(_c, "Ingiza namba ya simu sahihi!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_dbHandler.getUserPhone().matches(_phone)) {

            Toast.makeText(_c, "Namba ya simu hii ishatumika!", Toast.LENGTH_SHORT).show();
            return true;

        } else if (_password.isEmpty()) {
            Toast.makeText(_c, "Ingiza password!", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private String validatePhone() {
        String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        return pattern;
    }

    private void saveUserToServer() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("register");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                    try {
                        Log.d("RESPONSE", response);

                        Response res = _gson.fromJson(response, Response.class);
                        if (res.getCode() == 201) {

                            Success success = res.getSuccess();
                            User user = success.getUser();
                            String token = success.getToken();

                            _dbHandler.createUser(new User(0,user.getPhone(), user.getName(),  user.getId(),0));

                            //store token
                            _editor.putInt("USER_ID", user.getId());
                            _editor.putString("USER_TOKEN", token);
                            _editor.commit();

                            new FragmentHelper(_c).replace(new BusinessRegistrationFragment(), "BusinessRegistrationFragment", R.id.fragment_placeholder);


                        } else if(res.getCode() == 409) {
                            Toast.makeText(_c, "Namba ya simu imeshasajiliwa!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                        }
                        _registerBtn.setClickable(true);

                    } catch (NullPointerException e) {

                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                        Log.d("error", e.toString());
                    }

                },
                new com.android.volley.Response.ErrorListener() {
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
                            Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", _name);
                params.put("password", _password);
                params.put("phone", _phone);
                params.put("pin", "1234");
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
      //  postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) _c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
