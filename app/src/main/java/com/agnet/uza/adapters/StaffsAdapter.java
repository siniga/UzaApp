package com.agnet.uza.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.fragments.staffs.EditStaffFragment;
import com.agnet.uza.fragments.staffs.NewStaffFragment;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.User;

import java.util.Collections;
import java.util.List;

/**
 * Created by alicephares on 8/5/16.
 */
public class StaffsAdapter extends RecyclerView.Adapter<StaffsAdapter.ViewHolder> {

    private List<User> staffs = Collections.emptyList();
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
    public StaffsAdapter(Context c, List<User> staffs) {
        this.staffs = staffs;
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
        View v = inflator.inflate(R.layout.card_staffs, parent, false);
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
        final User currentStaff = staffs.get(position);


        holder.mName.setText(currentStaff.getName());

        //check snyc status
       /* if(currentStaff.getSyncStatus() == 0){
            holder.mSyncStatusFail.setVisibility(View.VISIBLE);
            holder.mSyncStatusOk.setVisibility(View.GONE);
        }else {
            holder.mSyncStatusFail.setVisibility(View.GONE);
            holder.mSyncStatusOk.setVisibility(View.VISIBLE);
        }*/

        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _editor.putInt("USER_SERVER_ID", currentStaff.getServerId());
                _editor.putInt("USER_ID", currentStaff.getId());
                _editor.commit();

                new FragmentHelper(c).replace(new EditStaffFragment(),"ditStaffFragment", R.id.fragment_placeholder);

            }
        });


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName;
        public View mBorderBtm;
        public ImageView mSyncStatusOk,mSyncStatusFail;

        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.wrapper);
            mName = view.findViewById(R.id.name);
            mSyncStatusOk = view.findViewById(R.id.sync_status_ok);
            mSyncStatusFail = view.findViewById(R.id.sync_status_failed);


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
        return staffs.size();
    }


}