package com.agnet.uza.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Product;
import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    private List<Product> products = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private List productlist = new ArrayList();
    private int cartItemCounts = 0;
    private int index = -1;
    private AlertDialog _alertDialog;
    private HomeFragment fragment;
    private DatabaseHandler _dbHandler;


    // Provide a suitable constructor (depends on the kind of dataset)
    public PopularProductAdapter(Context c, List<Product> products, HomeFragment fragment) {
        this.products = products;
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
        View v = inflator.inflate(R.layout.card_popular_product, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Product currentProduct = products.get(position);
        final int[] count = {1};

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        int formatedPrice = Integer.parseInt(currentProduct.getPrice());

        holder.mName.setText(currentProduct.getName());
        holder.mPrice.setText("TZS: " +formatter.format(formatedPrice));

        try {
            Glide.with(c)
                    .load(new URL(currentProduct.getImgUrl()))
                    .into(holder.mImg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mWrapper, mQntyviewWrapper;
        public TextView mName, mPrice,mSku;
        public EditText mQnty;
        public ImageView mImg;
        public ImageButton mBtnAddToCart;
        public Button mQntyViewRemove, mQntyViewAdd;


        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.shop_wrapper);
            mName = view.findViewById(R.id.name);
            mPrice = view.findViewById(R.id.price);
           // mBtnAddToCart = view.findViewById(R.id.add_to_cart_btn);
           // mQntyViewAdd = view.findViewById(R.id.quantity_view_add);
            //mQntyViewRemove = view.findViewById(R.id.quantity_view_remove);
            //mQntyviewWrapper = view.findViewById(R.id.qnty_view_wrapper);
            //mQnty = view.findViewById(R.id.quantity);
            mImg = view.findViewById(R.id.product_img);
            mSku = view.findViewById(R.id.sku);
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void filterList(List<Product> filterdProducts) {
        this.products = filterdProducts;
        notifyDataSetChanged();

    }


    public int getImage(String imageName) {

        int drawableResourceId = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());

        return drawableResourceId;
    }

}