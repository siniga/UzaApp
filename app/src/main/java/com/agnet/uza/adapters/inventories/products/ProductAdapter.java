  package com.agnet.uza.adapters.inventories.products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agnet.uza.R;
import com.agnet.uza.pages.HomeFragment;
import com.agnet.uza.helpers.DatabaseHandler;
import com.agnet.uza.models.Cart;
import com.agnet.uza.models.Order;
import com.agnet.uza.models.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alicephares on 8/5/16.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products = Collections.emptyList();
    private LayoutInflater inflator;
    private Context c;
    private int locateId;
    private SharedPreferences _preferences;
    private SharedPreferences.Editor _editor;
    private List productlist = new ArrayList();
    private int cartItemCounts = 0;
    private int index = -1;
    private AlertDialog _alertDialog;
    private HomeFragment fragment;
    private DatabaseHandler _dbHandler;
    private int productListType = 0;
    private Gson gson;

    private ProductItemActionListener actionListener;
    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductAdapter(Context c, List<Product> products, HomeFragment fragment, int productListType) {
        this.products = products;
        this.inflator = LayoutInflater.from(c);
        this.c = c;
        this.fragment = fragment;
        this.productListType = productListType;

        _preferences = c.getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        _editor = _preferences.edit();

        _dbHandler = new DatabaseHandler(c);
        gson = new Gson();

    }

    public void setActionListener(ProductItemActionListener actionListener) {
        this.actionListener = actionListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = inflator.inflate(R.layout.card_popular_product, parent, false);
        // set the view's size, margins, padding and layout parameters

        ViewHolder vh = new ViewHolder(c, v);
        return vh;
    }

    double totalAmount = 0;
    final int[] count = {0};
    List<Cart> carts = new ArrayList<>();

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //get a position of a current saleItem
        final Product currentProduct = products.get(position);

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        double formattedPrice = currentProduct.getPrice();

        final int[] stock = {currentProduct.getStock()};

        holder.mName.setText(currentProduct.getName());
        holder.mPrice.setText("TZS: " + formatter.format(formattedPrice));
        holder.mStcok.setText("" + currentProduct.getStock());

        /*holder.mItemIv.setImageResource(currentProduct.getResourceId());
        holder.mItemCopy.setImageResource(currentProduct.getResourceId());*/

   /*     holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        //TODO: store everything in a list when still manipulating cart data, before saving to db
        holder.mWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:avoid query the database everytime you receive user inputs
                if (count[0] >= 0) {

                    //out of stock
                    if (stock[0] > 0) {

                        stock[0]--;
                        count[0]++;

                        totalAmount = totalAmount + currentProduct.getPrice();
                        _dbHandler.updateProductStock(stock[0], currentProduct.getId());
                        int currentOrderId;

                       if(!_dbHandler.isValueExist(0,"orders","status")){

                           _dbHandler.createOrder(new Order(0,"","023883",0), 1);

                           currentOrderId = _dbHandler.getCurrentOrderId();

                       }else {
                           currentOrderId = _dbHandler.getCurrentOrderId();
                       }

                        if(actionListener!=null)
                            actionListener.onItemTap(holder.mImg);


                        if (!_dbHandler.isProductIdExist(currentProduct.getId())) {

                           // Toast.makeText(c, "Product doesnt exist", Toast.LENGTH_SHORT).show();
                            //individual item count
                            _dbHandler.createCart(new Cart(0, currentProduct.getPrice(), 1, currentProduct.getId(), currentProduct.getName(), currentProduct.getPrice()),currentOrderId);

                        } else {

                            double itemTotal = _dbHandler.getCartAmount(currentProduct.getId()) + currentProduct.getPrice();
//

                            int itemCount = _dbHandler.getCartQnty(currentProduct.getId());

                            _dbHandler.updateCart(new Cart(0, itemTotal, ++itemCount, currentProduct.getId(), currentProduct.getName(), currentProduct.getPrice()), currentOrderId);

                        }

                    } else {
                        ((HomeFragment) fragment).launchStockLowDialog(currentProduct.getName());
                    }

                    //show low stock msg
                    if (stock[0] <= 5) {
                        holder.mLowStock.setVisibility(View.VISIBLE);
                    }

                }


                holder.mStcok.setText("" + stock[0]);

                ((HomeFragment) fragment).addQntyCount();
                ((HomeFragment) fragment).addAmount();

            }
        });

       // displayImg(currentProduct.getImgUrl(), holder.mImg);

    }

    private void displayImg(String imgUrl, ImageView imgView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_product);
//        requestOptions.error(R.drawable.ic_error);

        Glide.with(c)
                .load(imgUrl)
                .apply(requestOptions)
                .into(imgView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mWrapper;
        public TextView mName, mPrice, mSku, mStcok;
        public EditText mQnty;
        public ImageView mImg, mLowStock;


        public ViewHolder(Context context, View view) {
            super(view);

            mWrapper = view.findViewById(R.id.shop_wrapper);
            mName = view.findViewById(R.id.name);
            mPrice = view.findViewById(R.id.price);
            mImg = view.findViewById(R.id.product_img);
            mSku = view.findViewById(R.id.sku);
            mStcok = view.findViewById(R.id.stock);
            mLowStock = view.findViewById(R.id.low_stock_msg);

        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void filterList(List<Product> filterdProducts) {
        this.products = filterdProducts;
        notifyDataSetChanged();

    }


    public int getImage(String imageName) {

        int drawableResourceId = c.getResources().getIdentifier(imageName, "drawable", c.getPackageName());

        return drawableResourceId;
    }



    public interface ProductItemActionListener{
        void onItemTap(ImageView imageView);
    }
}
