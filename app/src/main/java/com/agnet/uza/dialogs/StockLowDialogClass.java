package com.agnet.uza.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.agnet.uza.R;

public class StockLowDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    public Button continueBtn;
    private TextView nameTxt;
    private String stockName;

    public StockLowDialogClass(Activity a) {
        super(a);
        this.c = a;
        d =this;
    }

    public void setStockName(String name){
        this.stockName = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_low_stock_msg);

        nameTxt = findViewById(R.id.out_stock_msg);
        continueBtn = findViewById(R.id.btn_yes);

        nameTxt.setText(stockName);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();

            }
        });


    }
}