package com.agnet.uza.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agnet.uza.R;
import com.agnet.uza.adapters.CategoryProductAdapter;
import com.agnet.uza.adapters.ProductAdapter;
import com.agnet.uza.adapters.UnitAdapter;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.helpers.AppManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.StatusBarHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.ResponseData;
import com.agnet.uza.models.Unit;
import com.agnet.uza.service.Endpoint;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ProductsFragment extends Fragment {


    private FragmentActivity _c;
    private RecyclerView _productList, _categoryList, _unitList;
    private LinearLayoutManager _categoryLayoutManager, _unitLayoutManager;
    private UnitAdapter _unitAdapter;
    private GridLayoutManager _productLayoutManager;
    private ProductAdapter _productAdapter;
    private CategoryProductAdapter _categoryAdapter;
    private TextView _itemCounter;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private Gson _gson;
    private List<Category> _categories;
    private DatabaseHandler _dbHandler;
    private LinearLayout _errorMsg;
    private ShimmerFrameLayout _shimmerFrameLayout;
    private BottomNavigationView _bottomNavigation;
    private String _categoryName;
    private TextView _categoryNameVIew;
    private EditText _searchItemEditTxt;
    private List<Product> _products;;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _gson = new Gson();
        _products = new ArrayList<>();


        //binding
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _unitList = view.findViewById(R.id.unit_list);
        _searchItemEditTxt = view.findViewById(R.id.search_product_edittxt);
        _productList = view.findViewById(R.id.product_list);

        //
        try {

            //receive data from other activities and fragments
            _categoryName = _preferences.getString("CATEGORY_NAME", null);

            _shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);

            _errorMsg = view.findViewById(R.id.error_msg);

            _categoryNameVIew = view.findViewById(R.id.category_name);
            _categoryNameVIew.setText(_categoryName);

        } catch (NullPointerException ex) {
            ex.printStackTrace();

        }


        //Product list setup
        _productList.setHasFixedSize(true);
        _productList.getPreserveFocusAfterLayout();

        _productLayoutManager = new GridLayoutManager(_c, 2);
        _productList.setLayoutManager(_productLayoutManager);

        //unit list
        _unitList.setHasFixedSize(true);

        _unitLayoutManager = new LinearLayoutManager(_c, LinearLayoutManager.HORIZONTAL, false);
        _unitList.setLayoutManager(_unitLayoutManager);

        _unitAdapter = new UnitAdapter(_c, getUnits(), ProductsFragment.this);
        _unitList.setAdapter(_unitAdapter);

        getProducts();
        new StatusBarHelper(_c).setStatusBarColor(R.color.colorPrimary);
        addEditTxtChangeListener();

        return view;

    }

    public void addEditTxtChangeListener() {

        _searchItemEditTxt .addTextChangedListener(new TextWatcher() {

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

    private List<Unit> getUnits() {
        List<Unit> units = new ArrayList<>();
        units.add(new Unit(1, "Bucket", "", 0));
        units.add(new Unit(1, "Bottle", "", 0));
        units.add(new Unit(1, "Crate", "", 0));

        return units;
    }


    public void getProducts() {

        _shimmerFrameLayout.stopShimmerAnimation();
        _shimmerFrameLayout.setVisibility(View.GONE);


        _products = new ArrayList<>();
        _products.add(new Product(1, "Castle Light", "2500", "1800", "", 0, 0,  "https://images.pexels.com/photos/5785656/pexels-photo-5785656.png", 1,""));
        _products.add(new Product(2, "Grand Malt", "12500", "10000", "", 0, 0,  "https://images.pexels.com/photos/5785906/pexels-photo-5785906.png", 1,""));
        _products.add(new Product(3, "Budweiser", "5000", "3000", "", 0, 0,  "https://images.pexels.com/photos/5785676/pexels-photo-5785676.png", 1,""));
        _products.add(new Product(4, "Hennessy", "22500", "20000", "", 0, 0,  "http://cdn2.bigcommerce.com/server5500/tpbc2s65/products/1048/images/1081/HennessyVS__54694__45350.1358534173.1280.1280.jpg",  1,""));


        _productAdapter = new ProductAdapter(_c, _products, new HomeFragment(), 0);
        _productList.setAdapter(_productAdapter);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*
        TabLayout tabLayout = (TabLayout) _c.findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);*/
    }

    @Override
    public void onResume() {
        super.onResume();


        //show back button
      /*  ((MainActivity1) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((OrderActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);*/


    }


    @Override
    public void onPause() {
        super.onPause();
        _shimmerFrameLayout.stopShimmerAnimation();
    }


    public void getProductsByCategory(int id) {

        _shimmerFrameLayout.setVisibility(View.VISIBLE);
        _shimmerFrameLayout.startShimmerAnimation();
        _errorMsg.setVisibility(View.GONE);

        Endpoint.setUrl("mobile/products/" + id);
        String url = Endpoint.getUrl();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        _shimmerFrameLayout.stopShimmerAnimation();
                        _shimmerFrameLayout.setVisibility(View.GONE);

                        if (!AppManager.isNullOrEmpty(response)) {

                            ResponseData res = _gson.fromJson(response, ResponseData.class);

                            List<Product> products = res.getProducts();

                            if (products.size() != 0) {

                                _errorMsg.setVisibility(View.GONE);
                              /*  ProductAdapter productAdapter = new ProductAdapter(_c, products, ProductsFragment.this);
                                _productList.setAdapter(productAdapter);*/
                                // productAdapter.filterList(products);

                            } else {

                                _errorMsg.setVisibility(View.VISIBLE);
                            /*   _productAdapter = new ProductAdapter(_c, products, ProductsFragment.this);
                                _productList.setAdapter(_productAdapter);*/

                            }


                        } else {
                            Toast.makeText(_c, "Kuna tatizo, angalia mtandao alafu jaribu tena!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

//                        _progressBar.setVisibility(View.GONE);
                        NetworkResponse response = error.networkResponse;
                        String errorMsg = "";
                        if (response != null && response.data != null) {
                            String errorString = new String(response.data);
                            Log.i("log error", errorString);
                            //TODO: display errors based on the message from the server
                            Toast.makeText(_c, "Kuna tatizo, hakikisha mtandao upo sawa alafu jaribu tena!", Toast.LENGTH_LONG).show();

                        }

                        _shimmerFrameLayout.stopShimmerAnimation();
                        _shimmerFrameLayout.setVisibility(View.GONE);


                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(String.valueOf(response));
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        mSingleton.getInstance(_c).addToRequestQueue(postRequest);

        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
    }


}
