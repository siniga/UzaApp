package com.agnet.uza.pages.reports;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.inventories.products.TopProductAdapter;
import com.agnet.uza.helpers.ReportTimeFilterHelper;
import com.agnet.uza.models.TopProduct;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportFragment extends Fragment {

    private FragmentActivity _c;
    private Gson _gson;
    private LinearLayoutManager _topProductsLayoutManager;
    private RecyclerView _topProductsList;
    private Toolbar _toolbar,_homeToolbar;
    private BottomNavigationView _bottomNavigation;
    private ScrollView _scrollViewWrapper;
    private TextView _timeFilterBtn1,_timeFilterBtn2,_timeFilterBtn3,_timeFilterBtn4,_timeFilterBtn5;
    private LinearLayout _timeFilterWrapper;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        _c = getActivity();
        _topProductsList = view.findViewById(R.id.top_product_list);

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _scrollViewWrapper = view.findViewById(R.id.report_wrapper);
        _timeFilterWrapper = view.findViewById(R.id.time_filter_wrapper);
        _timeFilterBtn1 = view.findViewById(R.id.time_filter_btn1);
        _timeFilterBtn2 = view.findViewById(R.id.time_filter_btn2);
        _timeFilterBtn3 = view.findViewById(R.id.time_filter_btn3);
        _timeFilterBtn4 = view.findViewById(R.id.time_filter_btn4);
        _timeFilterBtn5 = view.findViewById(R.id.time_filter_btn5);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        //_bottomNavigation.setVisibility(View.GONE);

        _timeFilterBtn1.setBackgroundResource(R.drawable.round_corners_primary);
        _timeFilterBtn1.setTextColor(Color.parseColor("#ffffff"));

        //top products list
        _topProductsList.setHasFixedSize(true);
        _topProductsLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _topProductsList.setLayoutManager(_topProductsLayoutManager);

        _toolbar.setTitle("Reports");

        //event
        _scrollViewWrapper.pageScroll(View.FOCUS_UP);

        _timeFilterBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReportTimeFilterHelper(_c).changeBgColor(_timeFilterBtn1,_timeFilterWrapper );

            }
        });

        _timeFilterBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ReportTimeFilterHelper(_c).changeBgColor(_timeFilterBtn2,_timeFilterWrapper );


            }
        });

        _timeFilterBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReportTimeFilterHelper(_c).changeBgColor(_timeFilterBtn3,_timeFilterWrapper );

            }
        });

        _timeFilterBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReportTimeFilterHelper(_c).changeBgColor(_timeFilterBtn4,_timeFilterWrapper );

            }
        });

        _timeFilterBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReportTimeFilterHelper(_c).changeBgColor(_timeFilterBtn5,_timeFilterWrapper );


            }
        });


        getTopProducts();

        return view;

    }



    @Override
    public void onResume() {
        super.onResume();

        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onPause() {
        super.onPause();

    }


    public void getTopProducts() {
        List<TopProduct> products = new ArrayList<>();
        products.add(new TopProduct(1,20,"Eva bar soap","94500","1%"));
        products.add(new TopProduct(1,20,"Jet","44500","1%"));
        products.add(new TopProduct(1,20,"Safari Lager","64500","1%"));
        products.add(new TopProduct(1,20,"Whitedent","24500","1%"));


        TopProductAdapter productAdapter = new TopProductAdapter(_c,products);
        _topProductsList.setAdapter(productAdapter);

    }
}
