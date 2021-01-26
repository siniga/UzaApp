package com.agnet.uza.fragments.products;

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
import com.agnet.uza.dialogs.CustomDialogClass;
import com.agnet.uza.dialogs.ProductPhotoSelectorDialog;
import com.agnet.uza.fragments.SelectImageFragment;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.categories.NewCategoryFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.Sku;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class NewProductFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


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

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAPTURE_MEDIA = 368;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"RestrictedApi", "WrongConstant"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_product, container, false);
        _c = getActivity();

        //initialize
        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();
        _dbHandler = new DatabaseHandler(_c);

        //binding
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);

        _scrollView = view.findViewById(R.id.scrollview);
        _name = view.findViewById(R.id.name);
        _sellingPrice = view.findViewById(R.id.selling_price);
        _costPrice = view.findViewById(R.id.cost_price);
        _category = view.findViewById(R.id.category);
        _sku = view.findViewById(R.id.sku);
        _stock = view.findViewById(R.id.stock);
        _progressBar = view.findViewById(R.id.progress_bar);
        _saveProduct = view.findViewById(R.id.save_btn_wrapper);
        _newCategoryBtn = view.findViewById(R.id.new_cateogory);
        _selectImgBtnWrapper = view.findViewById(R.id.select_img_btn_wrapper);
        _selectImgBtn = view.findViewById(R.id.select_img_btn);
        _fab = _c.findViewById(R.id.fab);
        _imgPreview = view.findViewById(R.id.img_preview);
        _qrScannerWrapper = view.findViewById(R.id.qr_code_scanner_btn_wrapper);
        _qrScannerBtn = view.findViewById(R.id.qr_code_scanner_btn);
        _barcodeNumber = view.findViewById(R.id.barcode_number);

        //set items
        _homeToolbar.setVisibility(View.GONE);
        _toolbar.setVisibility(View.VISIBLE);
        _fab.setVisibility(View.GONE);

        _saveProduct.setClickable(true);

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

                new FragmentHelper(_c).replaceWithbackStack(new ProductQrCodeScannerFragment(), "ProductQrCodeScannerFragment", R.id.fragment_placeholder);
                break;
        }
    }

    private void launchCamera() {



    }



    private void newOpenCamera() {
        if (mThread == null) {
            mThread = new CameraHandlerThread();
        }

        synchronized (mThread) {
            mThread.openCamera(_c);
        }
    }

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
            _name.setText(_preferences.getString("ProductName", null));

        }

        if (_preferences.getString("ProductPrice", null) != "") {
            _sellingPrice.setText(_preferences.getString("ProductPrice", null));

        }
        if (_preferences.getString("ProductCost", null) != "") {
            _costPrice.setText(_preferences.getString("ProductCost", null));

        }
        if (_preferences.getString("ProductStock", null) != "") {
            _stock.setText(_preferences.getString("ProductStock", null));

        }

    }

    public void validateProductDetails() {

        _saveProduct.setClickable(false);

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


        //TODO: add discount from customers
        saveProductDetails(new Product(0, name, price, cost, barcode, 0, Integer.parseInt(stock), img, 1, "", ""));

        if (!_sku.getText().toString().isEmpty()) {

            //add sku then attach it to a currently added product
            Sku sku = new Sku(0, _sku.getText().toString(), "", 0);
            _dbHandler.createSku(sku);

            int skuLastId = _dbHandler.getLastId("skus");
            int productLastId = _dbHandler.getLastId("products");

            //assign sku to product
            _dbHandler.attachSkuToProduct(skuLastId, productLastId);
        }

        _progressBar.setVisibility(View.VISIBLE);
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
        }, 2000);


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
                _saveProduct.setClickable(true);
            }
        });
    }

    public void saveProductDetails(Product product) {

        _dbHandler.createProduct(product);
    }

    public void displaySelectedImg(Bitmap img) {
        _customDialog.dismiss();
    }

    private void showPhotoSelectorDialog() {
        FragmentManager fm = _c.getSupportFragmentManager();
        _customDialog = new ProductPhotoSelectorDialog();
        _customDialog.setParentFragment(new NewProductFragment());

        _customDialog.show(fm, "photo_selector");
    }

    private void saveEditTextValues() {

        if (!_name.getText().toString().isEmpty()) {
            _editor.putString("ProductName", _name.getText().toString());

        }
        if (!_sellingPrice.getText().toString().isEmpty()) {
            _editor.putString("ProductPrice", _sellingPrice.getText().toString());

        }

        if (!_costPrice.getText().toString().isEmpty()) {
            _editor.putString("ProductCost", _costPrice.getText().toString());


        }
        if (!_stock.getText().toString().isEmpty()) {
            _editor.putString("ProductStock", _stock.getText().toString());


        }

        _editor.commit();
    }

}
