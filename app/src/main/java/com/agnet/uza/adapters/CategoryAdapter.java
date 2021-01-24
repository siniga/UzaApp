package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.fragments.products.EditProductFragment;
import com.agnet.uza.fragments.products.NewProductFragment;
import com.agnet.uza.fragments.products.ProductsFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;

import java.util.Collections;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int index = -1;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private int lastSelectedPosition = -1;
    private DatabaseHandler _dbHandler;


    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(Context c, List<Category> categories) {
        this.categories = categories;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this._dbHandler = new DatabaseHandler(c);

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_category, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Category currentCategory = categories.get(position);

        holder.mName.setText(currentCategory.getName());
        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _editor.putString("CATEGORY_NAME",currentCategory.getName());
                _editor.commit();

                new FragmentHelper(c).replaceWithbackStack(new ProductsFragment(), "ProductsFragment", R.id.fragment_placeholder);

            }
        });

        //since only one radio button is allowed to be selected,
        // this condition un-checks previous selections
        holder.mRadioSelected.setChecked(lastSelectedPosition == position);
        holder.mRadioSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = position;
                notifyDataSetChanged();

                int productId = _preferences.getInt("SELECTED_PRODUCT_ID", 0);

                _dbHandler.updateProductCategoryId(productId, currentCategory.getId());

                new FragmentHelper(c).replace(new EditProductFragment(), "EditProductFragment", R.id.fragment_placeholder);

            }
        });


       /* int id = c.getResources().getIdentifier(currentCategory.getImgUrl(), "drawable",
                c.getPackageName());
        Drawable drawable = c.getResources().getDrawable(id, null);

        holder.mImg.setBackground(drawable);*/

    /*    try {
            Glide.with(c)
                    .load(new URL(currentCategory.getImgUrl()))
                    .into(holder.mImg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/
        /*Endpoint.setStorageUrl(currentCategory.getImgUrl());
        String url = Endpoint.getStorageUrl();

        try {
            Glide.with(c)
                    .load(new URL(currentCategory.getImgUrl()))
                    .into(holder.mImg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/
        if (index == -1) {
            index = position;

        }

       /* Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.mTransparentView.setBackgroundColor(color);*/
/*
        if (index == position) {

         *//*   ((ProductsFragment) fragment).getProductsByCategory(currentCategory.getServerId());*//*

//            holder.mName.setBackgroundResource(c.getResources().getIdentifier("round_corners_with_stroke_primary", "drawable", c.getPackageName()));
//            holder.mName.setTextColor(Color.parseColor("#008577"));
        } else {
//            holder.mName.setBackgroundResource(c.getResources().getIdentifier("round_corners_with_stroke_grey", "drawable", c.getPackageName()));
//            holder.mName.setTextColor(Color.parseColor("#666666"));
        }*/

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mWrapper;
        public TextView mName;
        public ImageView mImg;
        public LinearLayout mTransparentView;
        public RadioButton mRadioSelected;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.category_wrapper);
            mName = view.findViewById(R.id.category_name);
            mImg = view.findViewById(R.id.category_img);
            mTransparentView = view.findViewById(R.id.transparent_frontview);
            mRadioSelected = view.findViewById(R.id.category_select);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categories.size();
    }


}