package com.agnet.uza.pages.expenses.categories;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.pages.expenses.expenses.ExpensesFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.ExpensesCategory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class FilterExpCategoryAdapter extends RecyclerView.Adapter<FilterExpCategoryAdapter.ViewHolder> {

    private List<ExpensesCategory> expenses = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private int index = -1;
    private Fragment fragment;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private DatabaseHandler _dbHandler;

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public FilterExpCategoryAdapter(Context c, List<ExpensesCategory> expenses, Fragment fragment) {
        this.expenses = expenses;
        this.inflator = LayoutInflater.from(c);
        this.fragment = fragment;
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
        View v = inflator.inflate(R.layout.card_expenses_category, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }

    int count = 0;

    // Replace the contents of a
    //
    // view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final ExpensesCategory currentExpense = expenses.get(position);

        holder.mName.setText(currentExpense.getName());

        Glide.with(c).load(displayImg(holder.mImg, currentExpense.getImgUrl()))
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .error(R.drawable.ic_error)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.mImg);

    }

    private int displayImg(ImageView view, String url){
        Context context = view.getContext();
        int id = context.getResources().getIdentifier(url, "drawable", context.getPackageName());

        return id;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName, mInitial, mAmount;
        public ImageView mImg;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.wrapper);
            mName = view.findViewById(R.id.name);
            mImg = view.findViewById(R.id.circle_img);
            mAmount = view.findViewById(R.id.amount);
//


        }

        public void bind(final Category item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return expenses.size();
    }


}