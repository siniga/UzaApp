package com.agnet.uza.pages.inventories.products;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.application.mSingleton;
import com.agnet.uza.dialogs.CustomDialogClass;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.pages.SelectImageFragment;
import com.agnet.uza.pages.inventories.categories.NewCategoryFragment;
import com.agnet.uza.pages.inventories.InventoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.Response;
import com.agnet.uza.models.Sku;
import com.agnet.uza.models.Success;
import com.agnet.uza.service.Endpoint;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewInvProductFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private FragmentActivity _c;
    private Toolbar _toolbar, _homeToolbar;
    private Button _saveProduct, _updateProduct;
    private ProductPhotoSelectorDialog _customDialog;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private EditText _nameInput, _sellingPriceInput, _costPriceInput, _skuInput, _stockInput, _categoryInput;
    private String _name, _price, _cost, _stock, _sku, _barcode;
    private DatabaseHandler _dbHandler;
    private Product _product;
    private Spinner _category;
    private int _categoryPosition;
    private TextView _editCategory;
    private String _categoryName;
    private Button _newCategoryBtn, _changeCategoryBtn;
    private List<Category> _categoryList;
    private int _categoryId;
    private LinearLayout _selectImgBtnWrapper;
    private ImageButton _selectImgBtn;
    private ImageView _imgPreview;
    private LinearLayout _qrScannerWrapper;
    private ImageButton _qrScannerBtn;
    private TextView _barcodeNumber;
    private NestedScrollView _scrollView;
    private FloatingActionButton _fab;
    private ProgressBar _progressBar;
    private LinearLayout _transparentLoader;
    private Gson _gson;
    private String _TOKEN;
    private String _skuVal, _categoryVal;
    private LinearLayout _categoryWrapper;
    private String _catName;
    private BottomNavigationView _bottomNavigation;

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAPTURE_MEDIA = 368;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inv_fragment_new_product, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);
        _gson = new Gson();

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _scrollView = view.findViewById(R.id.scrollview);
        _nameInput = view.findViewById(R.id.name);
        _sellingPriceInput = view.findViewById(R.id.selling_price);
        _costPriceInput = view.findViewById(R.id.cost_price);
        _category = view.findViewById(R.id.category);
        _skuInput = view.findViewById(R.id.sku);
        _stockInput = view.findViewById(R.id.stock);
        _categoryInput = view.findViewById(R.id.category_input);
        _categoryWrapper = view.findViewById(R.id.category_wrapper);
        _saveProduct = view.findViewById(R.id.save_btn_wrapper);
        _newCategoryBtn = view.findViewById(R.id.new_cateogory);
        _selectImgBtnWrapper = view.findViewById(R.id.select_img_btn_wrapper);
        _selectImgBtn = view.findViewById(R.id.select_img_btn);
        _fab = _c.findViewById(R.id.fab);
        _imgPreview = view.findViewById(R.id.img_preview);
        _qrScannerWrapper = view.findViewById(R.id.qr_code_scanner_btn_wrapper);
        _qrScannerBtn = view.findViewById(R.id.qr_code_scanner_btn);
        _barcodeNumber = view.findViewById(R.id.barcode_number);
        _progressBar = view.findViewById(R.id.progress_bar);
        _transparentLoader = view.findViewById(R.id.transparent_loader);
        _fab = _c.findViewById(R.id.fab);

        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _fab.setVisibility(View.GONE);
        _bottomNavigation.setVisibility(View.GONE);

        _saveProduct.setClickable(true);

        try{
            if (_categoryList.size() == 0) {
                _categoryInput.setVisibility(View.VISIBLE);
                _categoryWrapper.setVisibility(View.GONE);
            } else {
                _categoryInput.setVisibility(View.GONE);
                _categoryWrapper.setVisibility(View.VISIBLE);
            }

        }catch (NullPointerException e){}


        _categoryList = _dbHandler.getCategories();
        ArrayAdapter categorySpinnerAdapter = new ArrayAdapter(_c, android.R.layout.simple_spinner_item, _categoryList);
        categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _category.setAdapter(categorySpinnerAdapter);

        _saveProduct.setVisibility(View.VISIBLE);
        _category.setVisibility(View.VISIBLE);
        _newCategoryBtn.setVisibility(View.VISIBLE);

        //events
        _saveProduct.setOnClickListener(this);
        _category.setOnItemSelectedListener(this);
        _newCategoryBtn.setOnClickListener(this);
        _selectImgBtnWrapper.setOnClickListener(this);
        _selectImgBtn.setOnClickListener(this);
        _selectImgBtnWrapper.setOnClickListener(this);
        _selectImgBtn.setOnClickListener(this);
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
                        saveEditTextValues();
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

        // saveEditTextValues();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_preferences.getString("BARCODE_NUM", null) != null) {
            _barcodeNumber.setText(_preferences.getString("BARCODE_NUM", null));

            _editor.remove("BARCODE_NUM");
            _editor.commit();

            _scrollView.post(new Runnable() {
                @Override
                public void run() {
                    _scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }


        //back arrows
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        _categoryPosition = position;
        _categoryName = _category.getSelectedItem().toString();
        _categoryId = _categoryList.get(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
         // if (requestCode == CAMERA_REQUEST) {
         //   Bitmap photo = (Bitmap) data.getExtras().get("data");
         //_imgPreview.setImageBitmap(photo);

         // Bitmap bmp = BitmapFactory.decodeFile(urImageFilePath);

         try {
             Uri selectedImageUri = data.getData();
             Log.e("selectedImageUri ", " = " + selectedImageUri);
             if (selectedImageUri != null) {

                 Bitmap bmp = BitmapFactory.decodeStream(_c.getContentResolver().openInputStream(selectedImageUri));
                 Log.e("bmp ", " = " + bmp);
                 _imgPreview.setImageBitmap(bmp);
                 Log.e("bmp ", " Displaying Imageview WIth Bitmap !!!!  = ");
             } else {
                 // If selectedImageUri is null check extras for bitmap
                 Bitmap bmp = (Bitmap) data.getExtras().get("data");
                 _imgPreview.setImageBitmap(bmp);
             }
         } catch (FileNotFoundException fe) {
             fe.printStackTrace();
         }
         Log.d("IMAGW", "" + data.getExtras().get("data"));
         //}
     }
 */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn_wrapper:
                validateProductDetails();
                break;
            case R.id.new_cateogory:
                saveEditTextValues();
                new FragmentHelper(_c).replaceWithbackStack(new NewCategoryFragment(), "NewCategoryFragment", R.id.fragment_placeholder);
                break;

            case R.id.select_img_btn_wrapper:
            case R.id.select_img_btn:

                _editor.putInt("CAMERA_FLAG", 1);
                _editor.commit();

                new FragmentHelper(_c).replaceWithbackStack(new SelectImageFragment(), "SelectImageFragment", R.id.fragment_placeholder);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        /*final int CHOOSE_CAMERA = 232;
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                        launchCamera();

                    }
                });

                break;
            case R.id.qr_code_scanner_btn_wrapper:
            case R.id.qr_code_scanner_btn:

                _editor.putInt("PRODUCT_ACTION_FLAG", 1);
                _editor.commit();

//                new FragmentHelper(_c).replaceWithbackStack(new ProductQrCodeScannerFragment(), "ProductQrCodeScannerFragment", R.id.fragment_placeholder);
                break;
        }
    }

    private void launchCamera() {
    }



/*
    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera(_c);
        }
    }
*/

    private CameraHandlerThread mThread = null;

    private static class CameraHandlerThread extends HandlerThread {
        Handler mHandler = null;

        CameraHandlerThread() {
            super("CameraHandlerThread");
            start();
            mHandler = new Handler(getLooper());
        }

        synchronized void notifyCameraOpened() {
            notify();
        }


        void openCamera(Activity c) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {


                }
            });
            try {
                wait();
            } catch (InterruptedException e) {
//                Log.w(LOG_TAG, "wait was interrupted");
            }
        }

    }

    private void resetEditTextValue() {
        if (_preferences.getString("ProductName", null) != "") {
            _nameInput.setText(_preferences.getString("ProductName", null));

        }

        if (_preferences.getString("ProductPrice", null) != "") {
            _sellingPriceInput.setText(_preferences.getString("ProductPrice", null));

        }
        if (_preferences.getString("ProductCost", null) != "") {
            _costPriceInput.setText(_preferences.getString("ProductCost", null));

        }
        if (_preferences.getString("ProductStock", null) != "") {
            _stockInput.setText(_preferences.getString("ProductStock", null));

        }

    }

    public void validateProductDetails() {

        _saveProduct.setClickable(false);

        _name = _nameInput.getText().toString();
        _price = _sellingPriceInput.getText().toString();
        _cost = _costPriceInput.getText().toString();
        _barcode = _barcodeNumber.getText().toString();
        _stock = _stockInput.getText().toString();
        _sku = _skuInput.getText().toString();

        if (_categoryList.size() == 0) {
            _catName = _categoryInput.getText().toString();
        } else {
            _catName = _categoryName;
        }

        if (_sku.isEmpty()) {
            _skuVal = "ww-ww-112-blue";
        } else {
            _skuVal = _sku;
        }

        if (!checkEmptyFields()) {
            //TODO: add discount from customers
            saveProduct(
                    new Product(0, _name, Double.parseDouble(_price), _cost, _barcode,
                            0, Integer.parseInt(_stock), "", 0, _catName, _skuVal, 0, 0)
            );


        }
   /*     if (!_sku.getText().toString().isEmpty()) {

            //add sku then attach it to a currently added product
            Sku sku = new Sku(0, _sku.getText().toString(), "", 0);
            _dbHandler.createSku(sku);

            int skuLastId = _dbHandler.getLastId("skus");
            int productLastId = _dbHandler.getLastId("products");

            //assign sku to product
            _dbHandler.attachSkuToProduct(skuLastId, productLastId);
        }*/

       /* _progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(_c, "Item Saved!", Toast.LENGTH_LONG).show();
        clearEditTextValue();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _saveProduct.setClickable(true);
                _progressBar.setVisibility(View.GONE);
                new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
            }
        }, 2000);*/


    }

    private boolean checkEmptyFields() {

        if (_name.isEmpty()) {
            launchErrorDialog();

            _nameInput.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _nameInput.setPadding(10, 0, 0, 0);

            return true;

        } else if (_price.isEmpty()) {
            launchErrorDialog();


            _sellingPriceInput.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _sellingPriceInput.setPadding(10, 0, 0, 0);
            return true;


        } else if (_cost.isEmpty()) {
            launchErrorDialog();

            _costPriceInput.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _costPriceInput.setPadding(10, 0, 0, 0);

            return true;


        } else if (_stock.isEmpty()) {
            launchErrorDialog();

            _stockInput.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
            _stockInput.setPadding(10, 0, 0, 0);

            return true;

        } else if (_catName.isEmpty()) {
            launchErrorDialog();

     /*   _category.setBackgroundResource(R.drawable.round_corners_with_stroke_red);
        _stockInput.setPadding(10, 0, 0, 0);
*/
            return true;

        } else {
            _nameInput.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
            _costPriceInput.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
            _sellingPriceInput.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
            _stockInput.setBackgroundResource(R.drawable.round_corners_with_stroke_grey_light);
            _nameInput.setPadding(10, 0, 0, 0);
            _costPriceInput.setPadding(10, 0, 0, 0);
            _stockInput.setPadding(10, 0, 0, 0);

            return false;
        }


    }

    private void clearEditTextValue() {

        if (_preferences.getString("ProductName", null) != "") {
            _editor.remove("ProductName");
            _nameInput.setText("");
        }

        if (_preferences.getString("ProductPrice", null) != "") {
            _editor.remove("ProductPrice");
            _sellingPriceInput.setText("");

        }
        if (_preferences.getString("ProductCost", null) != "") {
            _editor.remove("ProductCost");
            _costPriceInput.setText("");

        }
        if (_preferences.getString("ProductStock", null) != "") {
            _editor.remove("ProductStock");
            _stockInput.setText("");

        }

        _editor.commit();
    }

    private void launchErrorDialog() {
        CustomDialogClass dialog = new CustomDialogClass(_c, "");
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                _saveProduct.setClickable(true);
            }
        });
    }

    public void displaySelectedImg(Bitmap img) {
        _customDialog.dismiss();
    }

    private void showPhotoSelectorDialog() {
        FragmentManager fm = _c.getSupportFragmentManager();
        _customDialog = new ProductPhotoSelectorDialog();
        _customDialog.setParentFragment(new NewInvProductFragment());

        _customDialog.show(fm, "photo_selector");
    }

    private void saveEditTextValues() {

        if (!_nameInput.getText().toString().isEmpty()) {
            _editor.putString("ProductName", _nameInput.getText().toString());

        }
        if (!_sellingPriceInput.getText().toString().isEmpty()) {
            _editor.putString("ProductPrice", _sellingPriceInput.getText().toString());

        }

        if (!_costPriceInput.getText().toString().isEmpty()) {
            _editor.putString("ProductCost", _costPriceInput.getText().toString());


        }
        if (!_stockInput.getText().toString().isEmpty()) {
            _editor.putString("ProductStock", _stockInput.getText().toString());


        }

        _editor.commit();
    }

    private void saveProduct(Product product) {

        _progressBar.setVisibility(View.VISIBLE);
        _transparentLoader.setVisibility(View.VISIBLE);

        Endpoint.setUrl("product");
        String url = Endpoint.getUrl();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Response res = _gson.fromJson(response, Response.class);
                    if (res.getCode() == 201) {
                        Success success = res.getSuccess();
                        Product responseProduct = success.getProduct();
                        Category category = success.getCategory();
                        Sku sku = success.getSku();

                        if (!_dbHandler.isCategoryAvailable(category.getServerId())) {
                            _dbHandler.createCategory(new Category(0, category.getName(), "", category.getServerId(), category.getAvailabilityStatus()));
                        }

                        //check if sku is new, if it is store it locally,if not then ignore it
                        if (!_dbHandler.isSkuAvailable(sku.getServerId())) {//0 means new, 1 means exist
                            _dbHandler.createSku(new Sku(0, sku.getName(), "", sku.getServerId(), sku.getAvailabilityStatus()));

                        }

                        // Log.d("RESPONSE", _gson.toJson(success));
                        _dbHandler.createProduct(
                                new Product(0, responseProduct.getName(), responseProduct.getPrice(),
                                        responseProduct.getCost(), "", responseProduct.getDiscount(), responseProduct.getStock(),
                                        "", responseProduct.getCategoryId(), responseProduct.getCategory(),
                                        responseProduct.getSku(), responseProduct.getServerId(), responseProduct.getSkuId()
                                )
                        );

                    } else {

                        Toast.makeText(_c, "Kuna tatizo la mtandao, jaribu tena!", Toast.LENGTH_LONG).show();
                    }

                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);
                    _saveProduct.setClickable(true);

                    new FragmentHelper(_c).replace(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);

                },
                error -> {
                    _saveProduct.setClickable(true);
                    _progressBar.setVisibility(View.GONE);
                    _transparentLoader.setVisibility(View.GONE);

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
                params.put("name", product.getName());
                params.put("cost", product.getCost());
                params.put("price", "" + product.getPrice());
                params.put("stock", "" + product.getStock());
                params.put("category", "" + product.getCategory());
                params.put("sku", "" + product.getSku());
                params.put("discount", "" + 0);
                return params;
            }
        };

        mSingleton.getInstance(_c).addToRequestQueue(postRequest);
        // postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


}
