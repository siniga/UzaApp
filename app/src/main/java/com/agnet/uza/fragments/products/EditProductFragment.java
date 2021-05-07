package com.agnet.uza.fragments.products;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.dialogs.CustomDialogClass;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.fragments.inventories.ManageCategoryFragment;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.inventories.SelectCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.helpers.Validator;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Sku;
import com.agnet.uza.models.Success;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class EditProductFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct, _updateProductBtn;
    private ProductPhotoSelectorDialog _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _nameInput, _sellingPriceInput, _costPriceInput, _skuInput, _stockInput;
    private String _name, _price, _cost, _sku, _stock;
    private DatabaseHandler _dbHandler;
    private Product _product;
    private Spinner _category;
    private int _categoryPosition;
    private TextView _editCategory;
    private String _categoryName;
    private Button _changeCategoryBtn;
    private Gson _gson;
    private TextView _barcodeNumber;
    private LinearLayout _qrScannerWrapper;
    private ImageButton _qrScannerBtn;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private String _TOKEN;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);
        _gson = new Gson();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _nameInput = view.findViewById(R.id.name);
        _sellingPriceInput = view.findViewById(R.id.selling_price);
        _costPriceInput = view.findViewById(R.id.cost_price);
        _stockInput = view.findViewById(R.id.stock);
        _editCategory = view.findViewById(R.id.edit_category);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _updateProductBtn = view.findViewById(R.id.update_btn);
        _changeCategoryBtn = view.findViewById(R.id.change_category);
        _barcodeNumber = view.findViewById(R.id.barcode_number);
        _qrScannerWrapper = view.findViewById(R.id.qr_code_scanner_btn_wrapper);
        _qrScannerBtn = view.findViewById(R.id.qr_code_scanner_btn);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _updateProductBtn.setClickable(true);

        try {
            _product = _dbHandler.getProductById(_preferences.getInt("SELECTED_PRODUCT_ID", 0));
            _nameInput.setText(_product.getName());
            _sellingPriceInput.setText("" + _product.getPrice());
            _costPriceInput.setText(_product.getCost());
            _stockInput.setText("" + _product.getStock());
            _editCategory.setText(_product.getCategory());
        } catch (NullPointerException e) {
            _gson.toJson(e.getMessage());
        }

        //events
        _updateProductBtn.setOnClickListener(this);
        _changeCategoryBtn.setOnClickListener(this);
        _qrScannerBtn.setOnClickListener(this);
        _qrScannerWrapper.setOnClickListener(this);


        if (_preferences.getString("USER_TOKEN", null) != null) {
            _TOKEN = _preferences.getString("USER_TOKEN", null);
//            Log.d("TOKEN_HERE",""+_TOKEN);
        }


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_btn:
                _updateProductBtn.setClickable(false);
                validateProductDetails();
                break;
            case R.id.change_category:
                _editor.putInt("SELECTED_PRODUCT_ID", _product.getId());
                _editor.commit();
                new FragmentHelper(_c).replaceWithbackStack(new SelectCategoryFragment(), "SelectCategoryFragment", R.id.fragment_placeholder);
                break;
            case R.id.qr_code_scanner_btn_wrapper:
            case R.id.qr_code_scanner_btn:

                _editor.putInt("PRODUCT_ACTION_FLAG", 0);
                _editor.commit();

                // new FragmentHelper(_c).replaceWithbackStack(new ProductQrCodeScannerFragment(), "ProductQrCodeScannerFragment", R.id.fragment_placeholder);
                break;
        }
    }

    public void validateProductDetails() {
        _name = _nameInput.getText().toString();
        _price = _sellingPriceInput.getText().toString();
        _cost = _costPriceInput.getText().toString();
        _stock = _stockInput.getText().toString();
        //   _sku = _skuInput.getText().toString();

        LinkedHashMap product = new LinkedHashMap<String, Object>();
        product.put("Jina la bidhaa", _name);
        product.put("Bei ya kuuzia", _price);
        product.put("Bei ya kununua", _cost);
        product.put("Bidhaa zimebaki", _stock);
        // product.put("Sku",_sku);

        Validator validator = new Validator(_c);
        if (!validator.isEmpty(product, _updateProductBtn)) {
            saveProduct();
        }

    }


    private void saveProduct() {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("product/update");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {

                    Response res = _gson.fromJson(response, Response.class);
                    if (res.getCode() == 200) {
                        Success success = res.getSuccess();
                        Product product = success.getProduct();
                        Log.d("RESPONSE", _gson.toJson(product));

                        _dbHandler.updateProduct(new Product(0, product.getName(), product.getPrice(),
                                product.getCost(), "", product.getDiscount(), product.getStock(),
                                "", product.getCategoryId(), product.getCategory(),
                                product.getSku(), product.getServerId(), product.getSkuId())
                        );

                    } else {
                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _updateProductBtn.setClickable(true);

                    new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);

                },
                error -> {
                    _updateProductBtn.setClickable(true);
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

                    Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena au repoti tatizo!", Toast.LENGTH_LONG).show();

                    Log.d("RESPONSE_ERROR", "here" + error.getMessage());
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        String errorString = new String(response.data);
                        Log.i("log error", errorString);
                    }

                }
        ) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + _TOKEN);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", _name);
                params.put("cost", _cost);
                params.put("price", "" + _price);
                params.put("stock", "" + _stock);
                params.put("category", "" + _product.getCategoryId());
                params.put("sku", "ww-ww-2344-11");
                params.put("discount", "" + 0);
                params.put("id", "" + _product.getServerId());
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        // postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

}
