package com.agnet.uza.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.fragments.auth.BusinessRegistrationFragment;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {

    private DatabaseHandler _dbHandler;
    private Gson _gson;
    private String _name, _phone;
    private int SYNC_STATUS_ON = 1;
    private int SYNC_STATUS_OFF = 0;
    private String TAG = "NETWORKRECEIVER";
    private List<User> _users;
    private int FLAG_ADMIN = 1;


    @Override
    public void onReceive(Context context, Intent intent) {

        _dbHandler = new DatabaseHandler(context);
        _gson = new Gson();

        if (checkConnection(context)) {
            Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show();
            getSyncUsers(context);
        }else {
            Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
        }

    }

    private void getSyncUsers(Context context) {
        _users = _dbHandler.getUnsyncUsers();

        Log.d("BRECEIVER", _gson.toJson(_users));
        if (_users.size() > 0) {
            saveUserToServer(context);
        }
    }

    public boolean checkConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void saveUserToServer(Context context) {

        Endpoint.setUrl("register");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Synced!", Toast.LENGTH_SHORT).show();
                        try {
                            Response res = _gson.fromJson(response, Response.class);
                            Success success = res.getSuccess();
                            List<User> users = success.getUsers();

                            Log.d("BRECEIVER", _gson.toJson(users));
                            for (User user : users) {
                                _dbHandler.updateUserSyncStatus(new User(user.getId(), user.getPhone(), user.getName(), SYNC_STATUS_ON, user.getServerId(),user.getDeletedStatus()));
                            }

                        } catch (NullPointerException e) {

                            Log.d("error", e.toString());
                        }
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("users", _gson.toJson(_users));
                params.put("flag", "" + FLAG_ADMIN);
                return params;
            }
        };

        mSingleton.getInstance(context).addToRequestQueue(postRequest);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(30000
                , DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
