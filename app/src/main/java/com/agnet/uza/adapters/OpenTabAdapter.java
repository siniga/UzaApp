package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.models.Product;

import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class OpenTabAdapter extends RecyclerView.Adapter<OpenTabAdapter.ViewHolder> {

    private List<Product> products = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int index = -1;
    private Fragment fragment;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;


    // Provide a suitable constructor (depends on the kind of dataset)
    public OpenTabAdapter(Context c, List<Product> products, Fragment fragment) {
        this.products = products;
        this.inflator = LayoutInflater.from(c);
        this.fragment = fragment;
        this.c = c;


        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
    }

    @Override
    public int getItemViewType(int position) {

        //if chat is my chat or not
        //TODO: use my logged in stored id
        if (position == 0)
            return VIEW_TYPES.tabHeader;
        else
            return VIEW_TYPES.tabContent;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = null;

        switch (viewType) {
            case VIEW_TYPES.tabHeader:
                view = inflator.inflate(R.layout.card_open_tab_header, parent, false);
                break;
            case VIEW_TYPES.tabContent:
                view = inflator.inflate(R.layout.card_open_tab_content, parent, false);
                break;
            default:
                break;
        }
        return new ViewHolder(c, view);
    }

    int count = 0;

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Product currentProduct = products.get(position);

        int viewType = getItemViewType(position);

        switch (viewType) {

            case VIEW_TYPES.tabHeader: // handle row my msgs
                handleHeader(holder, currentProduct);
                break;
            case VIEW_TYPES.tabContent: // handle row their msgs
                handleContent(holder, currentProduct);
                break;

        }

    }

    private void handleContent(ViewHolder holder, Product currentProduct) {
        holder.mProuctName.setText(currentProduct.getName());
    }

    private void handleHeader(ViewHolder holder, Product currentProduct) {
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mProuctName;

        public ViewHolder(Context context, View view) {
            super(view);

            mProuctName  = view.findViewById(R.id.product_name);

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return products.size();
    }

    public class VIEW_TYPES {
        public static final int tabHeader = 1;
        public static final int tabContent = 2;
    }

}

