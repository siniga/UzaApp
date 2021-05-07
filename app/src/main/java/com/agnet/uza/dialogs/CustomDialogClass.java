package com.agnet.uza.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;

public class CustomDialogClass extends Dialog {

    public Context c;
    public Dialog d;
    public Button yes, no;
    private String msg;

    public CustomDialogClass(Context a,String msg) {
        super(a);
        this.c = a;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_error_msg);

        TextView  msgText = findViewById(R.id.error_msg);
        yes = findViewById(R.id.btn_yes);
        msgText.setText(msg);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });


    }
}