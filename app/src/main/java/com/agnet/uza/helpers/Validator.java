package com.agnet.uza.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.dialogs.CustomDialogClass;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Validator {
    private Gson _gson;
    private Context context;

    public Validator(Context c) {
        this.context = c;
        _gson = new Gson();
    }


    public boolean isEmpty(LinkedHashMap<String, String> values, Button btn) {
        for (Map.Entry<String,String> entry : values.entrySet()) {
            if (entry.getValue().isEmpty()) {
                launchErrorDialog("Sehemu ya "+entry.getKey()+" haitakiwi kuwa wazi!", btn);
                return true;
            }
        }
        return false;
    }

    private void launchErrorDialog(String msg, Button btn) {
        CustomDialogClass dialog = new CustomDialogClass(context, msg );
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(dialogInterface -> {
           btn.setClickable(true);
        });
    }



}
