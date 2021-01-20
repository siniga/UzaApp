package com.agnet.uza.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.helpers.DatabaseHandler;

public class CustomDialogDeleteProduct extends Dialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private int productId;
    private DatabaseHandler _dbHandler;

    public CustomDialogDeleteProduct(Activity a, int id) {
        super(a);
        this.c = a;
        d =this;
        _dbHandler = new DatabaseHandler(a);
        productId = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_product);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dbHandler.deleteProduct(productId);
                d.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });


    }
}