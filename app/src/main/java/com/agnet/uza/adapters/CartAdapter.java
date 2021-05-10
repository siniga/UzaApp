package com.agnet.uza.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.dialogs.DialogDeleteCartItem;
import com.agnet.uza.pages.checkout.CartFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Cart;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> cart = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int index = -1;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;
    private CartFragment fragment;


    // Provide a suitable constructor (depends on the kind of dataset)
    public CartAdapter(Context c, List<Cart> cart, CartFragment fragment) {
        this.cart = cart;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this.fragment = fragment;

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(c);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_cart, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Cart currentCart = cart.get(position);
        DecimalFormat formatter = new DecimalFormat("#,###,###");

        holder.mName.setText(currentCart.getProductName());
        holder.mQnty.setText("" + currentCart.getTotalQnty());
        holder.mPrice.setText("" + formatter.format(currentCart.getTotalAmount()));


        final int[] count = {_dbHandler.getCartQnty(currentCart.getProductId())};

        //calculate total price for the product
        int  currentOrderId = _dbHandler.getCurrentOrderId();

        holder.mIncrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++count[0];


                holder.mQnty.setText("" + count[0]);

                double itemTotal = _dbHandler.getCartAmount(currentCart.getProductId()) + currentCart.getOriginalPrice();
                _dbHandler.updateCart(new Cart(0, itemTotal, count[0], currentCart.getProductId(), currentCart.getProductName(), currentCart.getOriginalPrice()), currentOrderId);

                holder.mPrice.setText("" + formatter.format(itemTotal));
                fragment.updateTotalAmoutUi();

            }
        });

        holder.mDecrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count[0] > 1) {
                    --count[0];

                    holder.mQnty.setText("" + count[0]);

                    double itemTotal = _dbHandler.getCartAmount(currentCart.getProductId()) - currentCart.getOriginalPrice();
                    _dbHandler.updateCart(new Cart(0, itemTotal, count[0], currentCart.getProductId(), currentCart.getProductName(), currentCart.getOriginalPrice()),currentOrderId);

                    holder.mPrice.setText("" + formatter.format(itemTotal));
                    fragment.updateTotalAmoutUi();

                }
            }
        });

        holder.mDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDeleteCartItemDialog((Activity) c, currentCart.getId(),position);

            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mWrapper;
        public TextView mName, mQnty, mPrice;
        public Button mDecrementBtn, mIncrementBtn;
        public ImageView mDeleteItem;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.cart_wrapper);
            mName = view.findViewById(R.id.name);
            mQnty = view.findViewById(R.id.quantity);
            mPrice = view.findViewById(R.id.price);
            mDecrementBtn = view.findViewById(R.id.quantity_view_remove);
            mIncrementBtn = view.findViewById(R.id.quantity_view_add);
            mDeleteItem = view.findViewById(R.id.remove_cart_product);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cart.size();
    }

    public void launchDeleteCartItemDialog(Activity c, int cartItemId, int position) {
 
        DialogDeleteCartItem dialog = new DialogDeleteCartItem(c, cartItemId,this, position);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(dialogInterface -> {

            if(_dbHandler.isTableEmpty("carts")){

                fragment.showEmptyErrorMsg();
            }else{
                fragment.hideEmptyErrorMsg();
            }

            fragment.updateTotalAmoutUi();

          //  Toast.makeText(c, "Item Deleted!"+cartItemId, Toast.LENGTH_SHORT).show();
        });
    }

    public void removeAt(int position) {
        cart.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cart.size());
    }

}