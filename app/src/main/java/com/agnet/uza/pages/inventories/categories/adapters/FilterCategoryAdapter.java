package com.agnet.uza.pages.inventories.categories.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;

import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class FilterCategoryAdapter extends RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder> {

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
    private HomeFragment fragment;


    // Provide a suitable constructor (depends on the kind of dataset)
    public FilterCategoryAdapter(Context c, List<Category> categories) {
        this.categories = categories;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this._dbHandler = new DatabaseHandler(c);
        this.fragment = new HomeFragment();

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_home_category, parent, false);
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

        char init = currentCategory.getName().charAt(0);

        holder.mName.setText(currentCategory.getName());
        holder.mInitial.setText(""+init);


        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             _editor.putInt("CATEGORY_ID", currentCategory.getServerId());
             _editor.commit();

                Log.d("PRODUCITIO", currentCategory.getServerId()+" "+currentCategory.getId()+" "+currentCategory.getName());
             new FragmentHelper(c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);

            }
        });


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mWrapper;
        public TextView mName, mInitial;


        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.category_wrapper);
            mName = view.findViewById(R.id.category_name);
            mInitial = view.findViewById(R.id.initial);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return categories.size();
    }


}