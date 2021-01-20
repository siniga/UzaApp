package com.agnet.uza.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.CartAdapter;
import com.agnet.uza.models.Cart;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ActionBottomDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private LinearLayoutManager _categoryLayoutManager, _cartListManager;
    private RecyclerView _categoryList, _cartList;

    public static ActionBottomDialogFragment newInstance() {
        return new ActionBottomDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.btmsheet_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _cartList = view.findViewById(R.id.cart_list);

        //cart list
        _cartList.setHasFixedSize(true);
        _cartListManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        _cartList.setLayoutManager(_cartListManager);

        getCart();

    }


    public void getCart() {
        List<Cart> cartList = new ArrayList<>();
        cartList.add(new Cart("Castle Light", "3,000", "330ml", "can", 4));
        cartList.add(new Cart("Budweiser", "1,000", "330ml", "can", 4));
        cartList.add(new Cart("Uhai", "2,000", "330ml", "Plastic Bottle", 4));
        cartList.add(new Cart("Safari", "3,000", "330ml", "can", 4));

        CartAdapter adapter = new CartAdapter(getActivity(), cartList);
        _cartList.setAdapter(adapter);

    }

}