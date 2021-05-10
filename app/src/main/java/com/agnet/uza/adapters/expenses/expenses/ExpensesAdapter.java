package com.agnet.uza.adapters.expenses.expenses;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.pages.expenses.expenses.EditExpFragment;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.ExpensesItem;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private List<ExpensesItem> expenses= Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int index = -1;
    private Fragment fragment;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpensesAdapter(Context c, List<ExpensesItem> expenses) {
        this.expenses = expenses;
        this.inflator = LayoutInflater.from(c);
        this.fragment = fragment;
        this.c = c;


        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_expenses, parent, false);
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
        final ExpensesItem currentExpense = expenses.get(position);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        int formatedPrice = Integer.parseInt(currentExpense.getAmount());

        holder.mAmount.setText("TZS: " + formatter.format(formatedPrice));
        holder.mName.setText(currentExpense.getName());
        holder.mDate.setText(currentExpense.getDate());


        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _editor.putInt("EXPENSE_ID", currentExpense.getId());
                _editor.commit();

                new FragmentHelper(c).replaceWithbackStack(new EditExpFragment(),"EditExpenseFragment", R.id.fragment_placeholder);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName, mInitial,mAmount,mDate;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.wrapper);
            mName = view.findViewById(R.id.name);
            mAmount = view.findViewById(R.id.amount);
            mDate = view.findViewById(R.id.date);

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