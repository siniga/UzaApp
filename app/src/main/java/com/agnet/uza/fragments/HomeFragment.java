package com.agnet.uza.fragments;


import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.activities.MainActivity;
import com.agnet.uza.adapters.ProductAdapter;
import com.agnet.uza.dialogs.StockLowDialogClass;
import com.agnet.uza.fragments.auth.LoginFragment;
import com.agnet.uza.fragments.categories.CategoryFragment;
import com.agnet.uza.fragments.checkout.CartFragment;
import com.agnet.uza.fragments.checkout.ReceiptFragment;
import com.agnet.uza.fragments.inventories.InventoryFragment;
import com.agnet.uza.fragments.products.NewProductFragment;
import com.agnet.uza.helpers.AndroidDatabaseManager;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.helpers.FragmentHelper;
import com.agnet.uza.helpers.MyBounceInterpolator;
import com.agnet.uza.models.Product;
import com.agnet.uza.util.CircleAnimationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
/*import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;*/
//import com.pusher.client.channel.Channel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    //    private Channel _channel;
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
    private LinearLayout _sellBtn, _viewCartBtn;
    private Button _cartQnty, _totalAmount;
    private Toolbar _toolbar, _homeToolbar;
    private LinearLayout _cancelCategoryBtmsheetBtn, _cancelCartBtmsheet;
    private ImageButton _scannerBtn, _searchBtn;
    private LinearLayout _btnWrapper, _searchWrapper;
    private ImageView _cancelSearchBtn;
    private EditText _searchView;
    private List<Product> _products;
    private ProductAdapter _productAdapter;
    private FloatingActionButton _fab;
    private double _savedtotalAmnt;
    private Button _openInventoryBtn;
    private LinearLayout _emptyItemsMsg;
    private DecimalFormat _currencyformatter;
    private int _categoryId;
    private View view;
    private LinearLayout _toolbarIcon;
    private LinearLayout _checkoutBtn;
    private TextView _loggedInName;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        _c = getActivity();

        //initialization
        _dbHandler = new DatabaseHandler(_c);
        _mPhone = _dbHandler.getUserPhone();
        _notificationManager = NotificationManagerCompat.from(_c);
        _gson = new Gson();
        _currencyformatter = new DecimalFormat("#,###,###");

        _preferences = _c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        //binding
        _errorMsg = view.findViewById(R.id.error_msg);
        _bottomNavigation = _c.findViewById(R.id.bottom_navigation);
        _categoryBtn = view.findViewById(R.id.category_btn);
        _viewLoginPg = view.findViewById(R.id.view_user_login);
        _loggedInName = view.findViewById(R.id.logged_in_name);
        //  _viewCartBtn = view.findViewById(R.id.view_cart_btn);
        _totalAmount = view.findViewById(R.id.total_amount);
        _checkoutBtn = view.findViewById(R.id.checkout_main_btn);
        _cartQnty = view.findViewById(R.id.cart_qnty);
        _homeToolbar = _c.findViewById(R.id.home_toolbar);
        _toolbar = _c.findViewById(R.id.toolbar);
        _toolbarIcon = view.findViewById(R.id.toolbar_icon);
        _cancelCategoryBtmsheetBtn = _c.findViewById(R.id.cancel_category_btmsheet);
        _cancelCartBtmsheet = _c.findViewById(R.id.cancel_cart_btmsheet);
        _scannerBtn = view.findViewById(R.id.view_scanner_btn);
        _searchBtn = view.findViewById(R.id.search_btn);
        _cancelSearchBtn = view.findViewById(R.id.cancel_search_btn);
        _btnWrapper = view.findViewById(R.id.btn_wrapper);
        _searchWrapper = view.findViewById(R.id.search_wrapper);
        _searchView = view.findViewById(R.id.search_view);
        _productList = view.findViewById(R.id.product_list);
        _fab = _c.findViewById(R.id.fab);
        _openInventoryBtn = view.findViewById(R.id.open_product_inventory_btn);
        _emptyItemsMsg = view.findViewById(R.id.empty_items_msg);


        //set items
        _homeToolbar.setVisibility(View.VISIBLE);
        _toolbar.setVisibility(View.GONE);
        _bottomNavigation.setVisibility(View.VISIBLE);

        //popular products lists
        _productList.setHasFixedSize(true);
        _productLayoutManager = new LinearLayoutManager(_c, RecyclerView.VERTICAL, false);
        _productList.setLayoutManager(_productLayoutManager);

        //events
        _categoryBtn.setOnClickListener(this);
        _cartQnty.setOnClickListener(this);
        _totalAmount.setOnClickListener(this);
        _cancelCategoryBtmsheetBtn.setOnClickListener(this);
        _scannerBtn.setOnClickListener(this);
        _cancelSearchBtn.setOnClickListener(this);
        _searchBtn.setOnClickListener(this);
        _openInventoryBtn.setOnClickListener(this);

        //method calls
        ((MainActivity) _c).setHomeIconBottomNav();
        _loggedInName.setText(_dbHandler.getUserName());

        _scannerBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(_c, AndroidDatabaseManager.class);
                _c.startActivity(intent);

                return false;
            }
        });

        _toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FragmentHelper(_c).replaceWithbackStack(new MenuFragment(), " MenuFragment", R.id.fragment_placeholder);

            }
        });

        try {
            if (_preferences.getInt("CAMERA_FLAG", 0) == 1) {
                new FragmentHelper(_c).replaceWithbackStack(new NewProductFragment(), "NewProductFragment", R.id.fragment_placeholder);
                _editor.remove("CAMERA_FLAG");
                _editor.commit();

            }

            if (!_dbHandler.isTableEmpty("carts")) {

                _totalAmount.setText("TZS:" + _currencyformatter.format(_dbHandler.getCartTotalAmt()));
                _cartQnty.setText("" + _dbHandler.getCartTotalQnty());

            } else {
                _totalAmount.setText("0.0");
                _cartQnty.setText("0");
            }

            if (_preferences.getInt("CATEGORY_ID", 0) != 0) {

                _categoryId = _preferences.getInt("CATEGORY_ID", 0);
                _products = _dbHandler.getProductsByCategory(_categoryId);

            } else {

                _products = _dbHandler.getProducts();
            }

        } catch (NullPointerException e) {
            e.getMessage();
        }

        getProducts();

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
        _editor.remove("CATEGORY_ID");
        _editor.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.category_btn:
                new FragmentHelper(_c).replaceWithbackStack(new CategoryFragment(), "CategoryFragment", R.id.fragment_placeholder);
                break;
            case R.id.total_amount:
                new FragmentHelper(_c).replaceWithbackStack(new ReceiptFragment(), "PaymentFragment", R.id.fragment_placeholder);
                break;
            case R.id.cart_qnty:
                new FragmentHelper(_c).replaceWithbackStack(new CartFragment(), "CartFragment", R.id.fragment_placeholder);
                break;
            case R.id.view_scanner_btn:
              /*  Intent intent = new Intent(_c, ScannerActivity.class);
                _c.startActivity(intent);*/

                _editor.clear();
                _editor.commit();
                _dbHandler.deleteUser(1);
                new FragmentHelper(_c).replace(new LoginFragment(), "LoginFragment", R.id.fragment_placeholder);
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
            case R.id.open_product_inventory_btn:
                new FragmentHelper(_c).replaceWithbackStack(new InventoryFragment(), "InventoryFragment", R.id.fragment_placeholder);
                break;
            default:
                break;
        }
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
                        _c.finish();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("TOTAL_AMOUNT", _savedtotalAmnt);
    }

    /*  public void requestPermissions() {
          Dexter.withActivity(_c)
                  .withPermissions(
                          Manifest.permission.READ_CALENDAR,
                          Manifest.permission.RECORD_AUDIO)
  //                        Manifest.permission.ACCESS_FINE_LOCATION)
                  .withListener(new MultiplePermissionsListener() {
                      @Override
                      public void onPermissionsChecked(MultiplePermissionsReport report) {
                          // check if all permissions are granted
                          if (report.areAllPermissionsGranted()) {
                              // do you work now
                          }

                          // check for permanent denial of any permission
                          if (report.isAnyPermissionPermanentlyDenied()) {
                              // permission is denied permenantly, navigate user to app settings
                          }
                      }

                      @Override
                      public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                          token.continuePermissionRequest();
                      }
                  })
                  .onSameThread()
                  .check();
      }
  */
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

        try {
            //calling a method of the adapter class and passing the filtered list
            _productAdapter.filterList(filterdProducts);

        } catch (NullPointerException e) {

        }

    }


    public void getProducts() {

        if (_products.size() > 0) {
            _productList.setVisibility(View.VISIBLE);
            _emptyItemsMsg.setVisibility(View.GONE);

            _productAdapter = new ProductAdapter(_c, _products, this, 1);
            _productList.setAdapter(_productAdapter);
            _productAdapter.setActionListener(imageView -> {
                if (imageView != null)
                    makeFlyAnimation(imageView);
            });

        } else {

            _emptyItemsMsg.setVisibility(View.VISIBLE);
            _productList.setVisibility(View.GONE);

        }

        addEditTxtChangeListener();
    }

    private void makeFlyAnimation(ImageView targetView) {

//        LinearLayout destView = view.findViewById(R.id.cart);

        new CircleAnimationUtil().attachActivity(_c).setTargetView(targetView).setMoveDuration(300).setDestView(_cartQnty).setAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //addItemToCart();
                _cartQnty.setText("" + _dbHandler.getCartTotalQnty());
                _totalAmount.setText("TZS:" + _currencyformatter.format(_dbHandler.getCartTotalAmt()));

                targetView.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(_c, R.anim.bounce_animation);


                // Use bounce interpolator with amplitude 0.2 and frequency 20
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                _checkoutBtn.startAnimation(myAnim);
                // Toast.makeText(_c, "Continue Shopping...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();


    }

    public void addQntyCount() {

    }

    public void addAmount() {

    }

    public void launchStockLowDialog(String name) {
        StockLowDialogClass dialog = new StockLowDialogClass(_c);
        dialog.setStockName(name);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
//                _saveProduct.setClickable(true);
            }
        });
    }

}
