package com.agnet.uza.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.helpers.FragmentHelper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class SuccessFragment extends Fragment {

    private FragmentActivity _c;


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);
        _c = getActivity();


        TextView goBackBtn = view.findViewById(R.id.go_back_txt);
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentHelper(_c).replace(new HomeFragment(), "HomeFragment", R.id.fragment_placeholder);


            }
        });




        return view;

    }
}

