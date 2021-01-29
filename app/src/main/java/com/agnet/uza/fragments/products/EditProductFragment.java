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
import com.agnet.uza.dialogs.CustomDialogClass;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.fragments.inventories.ManageCategoryFragment;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.inventories.SelectCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.Sku;
import com.google.gson.Gson;


public class EditProductFragment extends Fragment implements View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct, _updateProduct;
    private ProductPhotoSelectorDialog _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _name, _sellingPrice, _costPrice, _sku, _stock;
    private DatabaseHandler _dbHandler;
    private LinearLayout _progressBar;
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

        _name = view.findViewById(R.id.name);
        _sellingPrice = view.findViewById(R.id.selling_price);
        _costPrice = view.findViewById(R.id.cost_price);
        _editCategory = view.findViewById(R.id.edit_category);
        _sku = view.findViewById(R.id.sku);
        _stock = view.findViewById(R.id.stock);
        _progressBar = view.findViewById(R.id.progress_bar);
        _updateProduct = view.findViewById(R.id.update_btn_wrapper);
        _changeCategoryBtn = view.findViewById(R.id.change_category);
        _barcodeNumber = view.findViewById(R.id.barcode_number);
        _qrScannerWrapper = view.findViewById(R.id.qr_code_scanner_btn_wrapper);
        _qrScannerBtn = view.findViewById(R.id.qr_code_scanner_btn);


        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);

        _updateProduct.setClickable(true);

        _product = _dbHandler.getProductById(_preferences.getInt("SELECTED_PRODUCT_ID", 0));


        try {

            _name.setText(_product.getName());
            _sellingPrice.setText(_product.getPrice());
            _costPrice.setText(_product.getCost());
            _stock.setText("" + _product.getStock());
            _sku.setText(_product.getSku());
            _barcodeNumber.setText(_product.getBarcode());

            if (_product.getCategory().isEmpty()) {
                _editCategory.setText("");
            } else {
                _editCategory.setText(_product.getCategory());
            }
        } catch (NullPointerException e) {
            e.getStackTrace();
        }


        //events
        _updateProduct.setOnClickListener(this);
        _changeCategoryBtn.setOnClickListener(this);
        _qrScannerBtn.setOnClickListener(this);
        _qrScannerWrapper.setOnClickListener(this);

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
                        clearEditTextValue();
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
            case R.id.update_btn_wrapper:
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

                new FragmentHelper(_c).replaceWithbackStack(new ProductQrCodeScannerFragment(), "ProductQrCodeScannerFragment", R.id.fragment_placeholder);
                break;
        }
    }


    public void validateProductDetails() {

        _updateProduct.setClickable(false);

        String name = _name.getText().toString();
        String price = _sellingPrice.getText().toString();
        String cost = _costPrice.getText().toString();
        String barcode = _barcodeNumber.getText().toString();
        String img = "";
        String stock = _stock.getText().toString();

        if (name.isEmpty()) {
            launchErrorDialog();

            _name.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _name.setPadding(10, 0, 0, 0);

            return;

        } else {
            _name.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
        }

        if (price.isEmpty()) {
            launchErrorDialog();

            _sellingPrice.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _sellingPrice.setPadding(10, 0, 0, 0);

            return;


        } else {
            _sellingPrice.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
        }

        if (cost.isEmpty()) {
            launchErrorDialog();

            _costPrice.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _costPrice.setPadding(10, 0, 0, 0);

            return;


        } else {
            _costPrice.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
        }

        if (stock.isEmpty()) {
            launchErrorDialog();

            _stock.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _stock.setPadding(10, 0, 0, 0);

            return;

        } else {

            _stock.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
        }


        updateProductDetails(new Product(_product.getId(), name, price, cost, barcode, 0,
                Integer.parseInt(stock), img, _product.getCategoryId(), "", ""
        ));


        boolean skuExist = _dbHandler.stringColumnExist(_sku.getText().toString(), "skus", "name");

        if (!_sku.getText().toString().isEmpty()) {

            if (!skuExist) {

                //add sku then attach it to a currently added product
                Sku sku = new Sku(0, _sku.getText().toString(), "", 0);
                _dbHandler.createSku(sku);

                //old sku
               int oldSkuId = _dbHandler.getSkuIdByName(_product.getSku());

                //new sku
                int newSkuId = _dbHandler.getLastId("skus");

                Log.d("SKU_TEST", ""+oldSkuId+" "+newSkuId+" "+_product.getId());

                //assign sku to product
                _dbHandler.reAttachSkuToProduct(newSkuId, oldSkuId, _product.getId());
            }
        }


        _progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(_c, "Item Saved!", Toast.LENGTH_LONG).show();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _updateProduct.setClickable(true);
                _progressBar.setVisibility(View.GONE);
                new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
            }
        }, 2000);


    }

    private void updateProductDetails(Product product) {
        _dbHandler.updateProduct(product);
    }

    private void launchErrorDialog() {
        CustomDialogClass dialog = new CustomDialogClass(_c);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                _updateProduct.setClickable(true);
            }
        });
    }

    private void clearEditTextValue() {

        if (_preferences.getString("ProductName", null) != "") {
            _editor.remove("ProductName");
            _name.setText("");
        }

        if (_preferences.getString("ProductPrice", null) != "") {
            _editor.remove("ProductPrice");
            _sellingPrice.setText("");

        }
        if (_preferences.getString("ProductCost", null) != "") {
            _editor.remove("ProductCost");
            _costPrice.setText("");

        }
        if (_preferences.getString("ProductStock", null) != "") {
            _editor.remove("ProductStock");
            _stock.setText("");

        }

        _editor.commit();
    }


}
