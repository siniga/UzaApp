package com.agnet.uza.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.fragments.HomeFragment;
import com.agnet.uza.helpers.AndroidDatabaseManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

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
    private int productListType = 0;


    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductAdapter(Context c, List<Product> products, HomeFragment fragment,int productListType) {
        this.products = products;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this.fragment = fragment;
        this.productListType = productListType;

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        _dbHandler = new DatabaseHandler(c);

    }

    @Override
    public int getItemViewType(int position) {

        //if chat is my chat or not
        //TODO: use my logged in stored id
        if (productListType == 0)
            return VIEW_TYPES.productByCategory;
        else
            return VIEW_TYPES.popularProduct;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = null;

        switch (viewType) {
            case VIEW_TYPES.productByCategory:
                view = inflator.inflate(R.layout.card_product, parent, false);
                break;
            case VIEW_TYPES.popularProduct:
                view = inflator.inflate(R.layout.card_popular_product, parent, false);
                break;
            default:
                break;
        }

        return new ViewHolder(c, view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Product currentProduct = products.get(position);
        final int[] count = {1};

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        int formatedPrice = Integer.parseInt(currentProduct.getPrice());

        holder.mName.setText(currentProduct.getName());
        holder.mPrice.setText("TZS: " + formatter.format(formatedPrice));

        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(c, AndroidDatabaseManager.class);
                c.startActivity(intent);
            }
        });


       displayImg(currentProduct.getImgUrl(), holder.mImg);

    }

    private void displayImg(String imgUrl, ImageView imgView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_product);
//        requestOptions.error(R.drawable.ic_error);

        Glide.with(c)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imgView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName, mPrice, mSku;
        public EditText mQnty;
        public ImageView mImg;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.shop_wrapper);
            mName = view.findViewById(R.id.name);
            mPrice = view.findViewById(R.id.price);
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

    public class VIEW_TYPES {
        public static final int productByCategory = 1;
        public static final int popularProduct = 2;
    }
}
