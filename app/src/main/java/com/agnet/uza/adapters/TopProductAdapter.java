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
import com.agnet.uza.models.TopProduct;
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
public class TopProductAdapter extends RecyclerView.Adapter<TopProductAdapter.ViewHolder> {

    private List<TopProduct> products = Collections.emptyList();
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
    public TopProductAdapter(Context c, List<TopProduct> products) {
        this.products = products;
        this.inflator = LayoutInflater.from(c);
        this.c = c;


        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        _dbHandler = new DatabaseHandler(c);

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_top_product, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final TopProduct currentProduct = products.get(position);


        DecimalFormat formatter = new DecimalFormat("#,###,###");
        int formatedAmnt = Integer.parseInt(currentProduct.getTotalAmount());

        holder.mName.setText(currentProduct.getName());
        holder.mTotalAmnt.setText(""+formatter.format(formatedAmnt));
        holder.mQnty.setText(""+currentProduct.getTotalQnty());



    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

           private TextView mName, mTotalAmnt,mQnty;

        public ViewHolder(Context context, View view) {
            super(view);
            mName = view.findViewById(R.id.name);
            mTotalAmnt = view.findViewById(R.id.total_amount);
            mQnty = view.findViewById(R.id.qnty);

        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void filterList(List<TopProduct> filterdProducts) {
        this.products = filterdProducts;
        notifyDataSetChanged();

    }


    public int getImage(String imageName) {

        int drawableResourceId = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());

        return drawableResourceId;
    }

}