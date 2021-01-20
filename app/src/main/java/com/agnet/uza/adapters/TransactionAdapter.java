package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.models.Cart;
import com.agnet.uza.models.ChildTransaction;
import com.agnet.uza.models.Date;
import com.agnet.uza.models.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions = Collections.emptyList();
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
    public TransactionAdapter(Context c, List<Transaction> transactions, List<Date> dates) {
        this.transactions = transactions;
        this.inflator = LayoutInflater.from(c);
        this.c = c;

        this.dates = dates;

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

    }

    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_transaction_items, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction currentTransaction = transactions.get(position);

        holder.mName.setText(currentTransaction.getDate());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mWrapper;
        public TextView mName, mQnty, mPrice;
        private LinearLayoutManager _transListManager;
        private RecyclerView transList;
        private Context context;



        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.category_wrapper);
            mName = view.findViewById(R.id.header_name);

            transList = view.findViewById(R.id.transaction_item_list);

            transList.setHasFixedSize(true);
            _transListManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            transList.setLayoutManager(_transListManager);

            getCart(context);


        }


        public void getCart(Context context) {
            List<ChildTransaction> childTransactions= new ArrayList<>();

            childTransactions.add(new ChildTransaction(1,"3300","11:00"));
            childTransactions.add(new ChildTransaction(1,"5000","11:30"));
            childTransactions.add(new ChildTransaction(1,"1400","12:48"));
            childTransactions.add(new ChildTransaction(1,"4300","14:02"));

            TransactionItemAdapter adapter = new TransactionItemAdapter(context, childTransactions);
            transList.setAdapter(adapter);


        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return transactions.size();
    }

}