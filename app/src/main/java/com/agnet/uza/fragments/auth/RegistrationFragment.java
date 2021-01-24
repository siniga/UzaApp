package com.agnet.uza.fragments.auth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.helpers.AndroidDatabaseManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Store;
import com.agnet.uza.models.Street;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class RegistrationFragment extends Fragment {

    private FragmentActivity _c;
    private TextView _signinLink;
    private Toolbar _toolbar;
    private Button _registerBtn;
    private DatabaseHandler _dbHandler;
    private EditText _phoneInputEdTxt, _nameInputEdTxt, _addresInput;
    private String _name, _phone, _address;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        _c = getActivity();

        //initializing
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _signinLink = view.findViewById(R.id.signin_link);
        _registerBtn = view.findViewById(R.id.register_btn);
        _phoneInputEdTxt = view.findViewById(R.id.phone_input);
        _nameInputEdTxt = view.findViewById(R.id.name_input);
        _addresInput = view.findViewById(R.id.address_input);

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
                _address = _addresInput.getText().toString();


                if (!checkEmptyFields()) {

                    _dbHandler.createUser(_phone, _name);
                    _dbHandler.createStreet(_address);

                    //get street last id and store it into business table to show its address
                    int lastId = _dbHandler.getLastId("streets");
                    _dbHandler.createBusiness(new Store(0,_name, new Street(lastId,"")));

                    //get business and user last id to connect user to their businesses
                    int userId = _dbHandler.getLastId("users");
                    int businessId = _dbHandler.getLastId("businesses");

                    _dbHandler.createUserBusiness(userId, businessId);

                    new FragmentHelper(_c).replaceWithbackStack(new CreatePinFragment(), "CreatePinFragment", R.id.fragment_placeholder);

                }


            }
        });

        return view;

    }

    private boolean checkEmptyFields() {
        if (_phone.isEmpty() || _phone.length() < 10) {

            Toast.makeText(_c, "Enter correct phone number!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_dbHandler.getUserPhone().matches(_phone)) {

            Toast.makeText(_c, "Phone is already available!", Toast.LENGTH_SHORT).show();
            return true;

        } else if (_name.isEmpty()) {
            Toast.makeText(_c, "Business name must not be empty!", Toast.LENGTH_LONG).show();
            return true;
        } else if (_address.isEmpty()) {
            Toast.makeText(_c, "Address must not be empty!", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private String validatePhone() {
        String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        return pattern;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


   /* public void getCategories() {

        _shimmerFrameLayout.setVisibility(View.VISIBLE);
        _shimmerFrameLayout.startShimmerAnimation();

        Endpoint.setUrl("mobile/categories");
        String url = Endpoint.getUrl();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (!AppManager.isNullOrEmpty(response)) {


                            _shimmerFrameLayout.stopShimmerAnimation();
                            _shimmerFrameLayout.setVisibility(View.GONE);

                            _editor.putString("RESPONSE", response);
                            _editor.commit();

                            ResponseData res = _gson.fromJson(response, ResponseData.class);
                            List<Category> categories = res.getCategories();

                            _dbHandler.addCategories(categories);

                            CategoryAdapter adapter = new CategoryAdapter(_c, categories, HomeFragment.this);
                            _categoryList.setAdapter(adapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        _shimmerFrameLayout.stopShimmerAnimation();
                        _shimmerFrameLayout.setVisibility(View.GONE);

                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                            //TODO: display errors based on the message from the server
                            Toast.makeText(_c, "Kuna tatizo, angalia mtandao alafu jaribu tena", Toast.LENGTH_SHORT).show();
                        }


                    }
                }
        ){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(String.valueOf(response));
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        mSingleton.getInstance(_c).addToRequestQueue(postRequest);

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }

*/
}
