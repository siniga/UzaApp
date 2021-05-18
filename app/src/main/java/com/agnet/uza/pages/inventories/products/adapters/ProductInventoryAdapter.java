package com.agnet.uza.pages.inventories.products.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.dialogs.CustomDialogDeleteProduct;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.pages.inventories.products.EditInvProductFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class ProductInventoryAdapter extends RecyclerView.Adapter<ProductInventoryAdapter.ViewHolder> {

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
    public ProductInventoryAdapter(Context c, List<Product> products, HomeFragment fragment, int productListType) {
        this.products = products;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this.fragment = fragment;
        this.productListType = productListType;

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        _dbHandler = new DatabaseHandler(c);

    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = null;
        view = inflator.inflate(R.layout.card_inventory_products, parent, false);


        return new ViewHolder(c, view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Product currentProduct = products.get(position);
        final int[] count = {1};

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        double formatedPrice = currentProduct.getPrice();

        holder.mName.setText(currentProduct.getName());
        holder.mPrice.setText("TZS: " + formatter.format(formatedPrice));
        holder.mStock.setText("" + currentProduct.getStock());
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogDeleteProduct dialog = new CustomDialogDeleteProduct((Activity) c, currentProduct.getId());
                dialog.show();
                dialog.setCancelable(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        products.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position,products.size());
                        Toast.makeText(c, "Product Deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _editor.putInt("INVENTORY_VIEW_FLAG", 1);
                _editor.putInt("SELECTED_PRODUCT_ID", currentProduct.getId());
                _editor.commit();

                new FragmentHelper(c).replace(new EditInvProductFragment(), "EditProductFragment", R.id.fragment_placeholder);

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
        public LinearLayout mWrapper, mDelete;
        public TextView mName, mPrice, mSku, mStock;
        public EditText mQnty;
        public ImageView mImg;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.shop_wrapper);
            mName = view.findViewById(R.id.name);
            mPrice = view.findViewById(R.id.price);
            mImg = view.findViewById(R.id.product_img);
            mSku = view.findViewById(R.id.sku);
            mStock = view.findViewById(R.id.stock);
            mDelete = view.findViewById(R.id.delete_product);
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
