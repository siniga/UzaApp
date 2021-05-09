package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.fragments.expenses.categories.EditCategoryFragment;
import com.agnet.uza.fragments.expenses.expenses.ExpensesFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.ExpensesCategory;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class ExpensesCategoryAdapter extends RecyclerView.Adapter<ExpensesCategoryAdapter.ViewHolder> {

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
    public ExpensesCategoryAdapter(Context c, List<ExpensesCategory> expenses, Fragment fragment) {
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

        try {
            if (currentExpense.getAmount().equals(null) || currentExpense.getAmount().isEmpty()) {
                holder.mAmount.setText("0.0");
            } else {
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                int formatedPrice = Integer.parseInt(currentExpense.getAmount());
//                holder.mAmount.setText("TZS: " + formatter.format(formatedPrice));

            }

        } catch (NullPointerException exception) {
            Log.e("log_error", exception.toString());
        }


        holder.mName.setText(currentExpense.getName());
        char initial = currentExpense.getName().charAt(0);

        //update expense list when position is 1 and display default data
        if(position == 1){
          ((ExpensesFragment) fragment).loadExpenseAdapter(currentExpense.getId());
        }


        holder.mWrapper.setOnClickListener(v -> {
            _editor.putInt("EXPCATEGORY_ID", currentExpense.getId());
            _editor.putString("EXPCATEGORY_NAME", currentExpense.getName());
            _editor.commit();

            new FragmentHelper(c).replaceWithbackStack(new EditCategoryFragment(), "EditExpensesFragment", R.id.fragment_placeholder);

        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName, mInitial, mAmount;
        public View mBorderBtm;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.wrapper);
            mName = view.findViewById(R.id.name);
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