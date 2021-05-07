package com.agnet.uza.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.fragments.CartFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Cart;

public class DialogDeleteCartItem extends Dialog {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    private DatabaseHandler _dbHandler;
    private int cartItemId;
    private CartAdapter adapter;
    private int position;


    public DialogDeleteCartItem(Activity a, int cartItemId, CartAdapter adapter, int position) {
        super(a);
        this.c = a;
        d =this;
        this.cartItemId = cartItemId;
        this.adapter = adapter;
        this.position = position;

        _dbHandler = new DatabaseHandler(a);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_delete_cart_item);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _dbHandler.deleteCartItem(cartItemId);
                ((CartAdapter) adapter).removeAt(position);

                Toast.makeText(c, "Bidhaa imefutwa!", Toast.LENGTH_SHORT).show();
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