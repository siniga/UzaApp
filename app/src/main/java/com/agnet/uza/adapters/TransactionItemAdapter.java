package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.models.ChildTransaction;
import com.agnet.uza.models.Date;
import com.agnet.uza.models.HistoryProduct;
import com.agnet.uza.models.Transaction;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class TransactionItemAdapter extends RecyclerView.Adapter<TransactionItemAdapter.ViewHolder > {

    private List<ChildTransaction> transactions = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int index = -1;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Date> dates;



    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionItemAdapter(Context c, List<ChildTransaction> transactions) {
        this.transactions = transactions;
        this.inflator= LayoutInflater.from(c);
        this.c = c;


        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
    }



    // Create new views (invoked by the layout manager)
    @Override
    public TransactionItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_transaction_child, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final TransactionItemAdapter.ViewHolder holder, final int position) {
        final ChildTransaction currentTrans = transactions.get(position);

        holder.totalAmnt.setText(currentTrans.getTotalAmount());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mWrapper;
        public TextView totalAmnt;



        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.category_wrapper);
            totalAmnt = view.findViewById(R.id.total_amount);

        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transactions.size();
    }


}