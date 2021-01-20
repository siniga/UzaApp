package com.agnet.uza.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.activities.ScannerActivity;
import com.agnet.uza.adapters.ProductAdapter;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Product;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.pusher.client.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity _c;

    private Gson _gson;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private TextView _errorMsg;
    private DatabaseHandler _dbHandler;
    private ShimmerFrameLayout _shimmerFrameLayout;
    private Channel _channel;
    private String _mPhone;
    private Handler _mHandler;
    private RecyclerView _productList;
    private NotificationManagerCompat _notificationManager;
    private LinearLayoutManager _productLayoutManager;
    private TextView _viewCategories;
    private BottomNavigationView _bottomNavigation;
    private ImageView _viewLoginPg;
    private TextView _viewDb, _viewScanner;
    private TextView _searchItemEditTxt;
    private Button _categoryBtn;
    private LinearLayout _sellBtn;
    private TextView _viewCartBtn;
    private Toolbar _toolbar, _homeToolbar;
    private LinearLayout _cancelCategoryBtmsheetBtn, _cancelCartBtmsheet;
    private ImageButton _scannerBtn, _searchBtn;
    private LinearLayout _btnWrapper, _searchWrapper;
    private ImageView _cancelSearchBtn;
    private EditText _searchView;
    private List<Product> _products;
    private ProductAdapter _productAdapter;
    private FloatingActionButton _fab;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        _c = getActivity();

        //binding
        _shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        _errorMsg = view.findViewById(R.id.error_msg);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _categoryBtn = view.findViewById(R.id.category_btn);
        _viewLoginPg = view.findViewById(R.id.view_user_login);
        _sellBtn = _c.findViewById(R.id.sell_btn);
        _viewCartBtn = _c.findViewById(R.id.view_cart_btn);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _cancelCategoryBtmsheetBtn = _c.findViewById(R.id.cancel_category_btmsheet);
        _cancelCartBtmsheet = _c.findViewById(R.id.cancel_cart_btmsheet);
        _scannerBtn = view.findViewById(R.id.view_scanner_btn);
        _searchBtn = view.findViewById(R.id.search_btn);
        _cancelSearchBtn = view.findViewById(R.id.cancel_search_btn);
        _btnWrapper = view.findViewById(R.id.btn_wrapper);
        _searchWrapper = view.findViewById(R.id.search_wrapper);
        _searchView = view.findViewById(R.id.search_view);
        _productList = view.findViewById(R.id.popular_product_list);
        _fab = _c.findViewById(R.id.fab);


        //initialization
        _dbHandler = new DatabaseHandler(_c);
        _mPhone = _dbHandler.getUserPhone();
        _notificationManager = NotificationManagerCompat.from(_c);
        _gson = new Gson();

        //set items
        _homeToolbar.setVisibility(View.VISIBLE);
        _toolbar.setVisibility(View.GONE);
        _bottomNavigation.setVisibility(View.VISIBLE);


        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //popular products lists
        _productList.setHasFixedSize(true);

        _productLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _productList.setLayoutManager(_productLayoutManager);


        //events
        _categoryBtn.setOnClickListener(this);
        _sellBtn.setOnClickListener(this);
        _viewCartBtn.setOnClickListener(this);
        _cancelCategoryBtmsheetBtn.setOnClickListener(this);
        _scannerBtn.setOnClickListener(this);
        _cancelSearchBtn.setOnClickListener(this);
        _searchBtn.setOnClickListener(this);

        //method calls
        getPopularProducts();
        addEditTxtChangeListener();
        ((MainActivity) _c).setHomeIconBottomNav();

        return view;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onResume() {
        super.onResume();
        _fab.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void getPopularProducts() {

        _products = _dbHandler.getProducts();
        _productAdapter = new ProductAdapter(_c, _products, this, 1);
        _productList.setAdapter(_productAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_btn:
                new FragmentHelper(_c).replaceWithbackStack(new CategoryFragment(), "CategoryFragment", R.id.fragment_placeholder);
                break;
            case R.id.sell_btn:
                new FragmentHelper(_c).replaceWithbackStack(new PaymentFragment(), "PaymentFragment", R.id.fragment_placeholder);
                break;
            case R.id.view_cart_btn:
                new FragmentHelper(_c).replaceWithbackStack(new CartFragment(), "CartFragment", R.id.fragment_placeholder);
                ;
                break;
            case R.id.view_scanner_btn:
                Intent intent = new Intent(_c, ScannerActivity.class);
                _c.startActivity(intent);
                break;
            case R.id.cancel_search_btn:
                _searchView.setText("");
                hideKeyboard(_c);
                _btnWrapper.setVisibility(View.VISIBLE);
                _searchWrapper.setVisibility(View.GONE);
                break;
            case R.id.search_btn:
                showKeyboard(_searchView);
                _btnWrapper.setVisibility(View.GONE);
                _searchWrapper.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    public static void showKeyboard(EditText editText) {
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addEditTxtChangeListener() {

        _searchView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                filterProducts(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {


            }
        });
    }


    private void filterProducts(String text) {
        //new array list that will hold the filtered data
        List<Product> filterdProducts = new ArrayList<>();

        //looping through existing elements
        for (Product product : _products) {
            //if the existing elements contains the search input
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdProducts.add(product);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        _productAdapter.filterList(filterdProducts);
    }

}
