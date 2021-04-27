package com.agnet.uza.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.agnet.uza.models.Cart;
import com.agnet.uza.models.ExpensesCategory;
import com.agnet.uza.models.ExpensesItem;
import com.agnet.uza.models.Order;
import com.agnet.uza.models.Business;
import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;
import com.agnet.uza.models.Sku;
import com.agnet.uza.models.Address;
import com.agnet.uza.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context c;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 39;

    // Database Name
    private static final String DATABASE_NAME = "uza";


    // table names
    private static final String TABLE_USER = "users";
    private static final String TABLE_ADDRESS = "addresses";
    private static final String TABLE_BUSINESS = "businesses";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_UNIT = "units";
    private static final String TABLE_SKU = "skus";
    private static final String TABLE_PRODUCT_UNIT = "product_units";
    private static final String TABLE_PRODUCT_SKU = "product_skus";
    private static final String TABLE_EXPENSES_CATEGORY = "expenses_categories";
    private static final String TABLE_EXPENSES_ITEM = "expense_items";
    private static final String TABLE_ORDER = "orders";
    private static final String TABLE_CART = "carts";

    //user table
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PIN = "pin";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SYNC_STATUS = "sync_status";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_DELETED_STATUS = "deleted_status";

    //address table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";

    //business
    private static final String KEY_BUSINESS_ID = "business_id";
    private static final String KEY_ADDRESS_ID = "address_id";

    //
    private static final String KEY_CATEGORY_ID = "category_id";

    //products
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_COST = "cost";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DISCOUNT = "discount";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_BARCODE = "barcode";

    //sku
    private static final String KEY_SKU_ID = "sku_id";
    private static final String KEY_UNIT_ID = "unit_id";

    //expense
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_EXPENSES_CATEGORY_ID = "expenses_categories_id";
    private static final String KEY_DATE = "date";

    //orders
    private static final String KEY_DEVICE_TIME = "device_order_time";
    private static final String KEY_ORDER_NO = "order_no";
    private static final String KEY_STATUS = "status";

    //carts
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_TOTAL_AMOUNT = "total_amount";
    private static final String KEY_ORDER_ID = "order_id";

    private Gson _gson;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
        _gson = new Gson();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PIN + " INTEGER,"
                + KEY_PHONE + " TEXT,"
                + KEY_SYNC_STATUS + " INTEGER,"
                + KEY_DELETED_STATUS + " INTEGER,"
                + KEY_SERVER_ID + " INTEGER " + ")";

        String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_SERVER_ID + " INTEGER " + ")";

        String CREATE_BUSINESS_TABLE = "CREATE TABLE " + TABLE_BUSINESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_ADDRESS_ID + " INTEGER,"
                + KEY_SERVER_ID + " INTEGER " + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_BUSINESS_ID + " INTEGER " + ")";

        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL,"
                + KEY_COST + " TEXT NOT NULL,"
                + KEY_PRICE + " TEXT NOT NULL,"
                + KEY_DISCOUNT + " INTEGER DEFAULT 0,"
                + KEY_STOCK + " INTEGER DEFAULT 0,"
                + KEY_IMAGE + " TEXT NOT NULL,"
                + KEY_BARCODE + " TEXT NOT NULL,"
                + KEY_CATEGORY_ID + " INTEGER DEFAULT 0 " + ")";


        String CREATE_SKU_TABLE = "CREATE TABLE " + TABLE_SKU + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";

        String CREATE_UNIT_TABLE = "CREATE TABLE " + TABLE_UNIT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY_ID + " INTEGER" + ")";

        String CREATE_PRODUCT_SKU_TABLE = "CREATE TABLE " + TABLE_PRODUCT_SKU + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SKU_ID + " INTEGER,"
                + KEY_PRODUCT_ID + " INTEGER" + ")";

        String CREATE_PRODUCT_UNIT_TABLE = "CREATE TABLE " + TABLE_PRODUCT_UNIT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UNIT_ID + " INTEGER,"
                + KEY_PRODUCT_ID + " INTEGER" + ")";

        String CREATE_EXPENSES_CATEGORY_TABLE = "CREATE TABLE " + TABLE_EXPENSES_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_AMOUNT + " TEXT " + ")";

        String CREATE_EXPENSES_ITEM_TABLE = "CREATE TABLE " + TABLE_EXPENSES_ITEM + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_EXPENSES_CATEGORY_ID + " INTEGER " + ")";

        String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ORDER_NO + " TEXT,"
                + KEY_DEVICE_TIME + " TEXT,"
                + KEY_STATUS + " INTEGER,"
                + KEY_BUSINESS_ID + " INTEGER " + ")";

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT_ID + " TEXT,"
                + KEY_TOTAL_AMOUNT + " TEXT,"
                + KEY_QUANTITY + " TEXT,"
                + KEY_ORDER_ID + " INTEGER " + ")";


        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_BUSINESS_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_SKU_TABLE);
        db.execSQL(CREATE_UNIT_TABLE);
        db.execSQL(CREATE_PRODUCT_SKU_TABLE);
        db.execSQL(CREATE_PRODUCT_UNIT_TABLE);
        db.execSQL(CREATE_EXPENSES_CATEGORY_TABLE);
        db.execSQL(CREATE_EXPENSES_ITEM_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_CART_TABLE);



    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SKU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_UNIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SKU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);
    }

    /*******************************************
     General functions
     ********************************************/

    public int getLastId(String table) {
        int lastId = 0;

        SQLiteDatabase db = this.getWritableDatabase();

        // Select All Query
        String selectQuery = " SELECT " + KEY_ID + " FROM " + table + " ORDER BY " + KEY_ID + " DESC limit 1";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {

            lastId = cursor.getInt(0);
        }
        db.close();
        return lastId;
    }

    public boolean isTableEmpty(String table) {

        boolean isEmpty;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT * FROM " + table;
        Cursor cursor = db.rawQuery(count, null);

        if (cursor.getCount() > 0) {
            isEmpty = false;
        } else {

            isEmpty = true;
        }

        /* Toast.makeText(c, ""+isEmpty, Toast.LENGTH_SHORT).show();*/

        return isEmpty;
    }

    public boolean stringColumnExist(String column, String table, String WhereClouse) {

        boolean exist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE " + WhereClouse + " =?", new String[]{column});


        if (c.getCount() > 0) {
            //   Log.d("LOGHERE", "exist");
            exist = true;
        } else {
            // Log.d("LOGHERE", "doesnt exist");
            exist = false;
        }
        return exist;

    }

    public boolean intColumnExist(int column, String table, String WhereClouse) {

        boolean exist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + table + " WHERE " + WhereClouse + " =?", new String[]{String.valueOf(column)});

        if (c.getCount() > 0) {
            Log.d("LOGHERE", "exist");
            exist = true;
        } else {
            Log.d("LOGHERE", "doesnt exist");
            exist = false;
        }
        return exist;

    }

    public boolean isValueExist(int val, String table, String WhereClouse) {

        boolean exist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT *  FROM " + table + " WHERE " + WhereClouse + " =?", new String[]{String.valueOf(val)});

        if (c.getCount() > 0) {
            Log.d("LOGHERE", "exist");
            exist = true;
        } else {
            Log.d("LOGHERE", "doesnt exist");
            exist = false;
        }
        return exist;

    }


    /*******************************************
     Begin user crude
     ********************************************/
    public void createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PIN, 0);
        values.put(KEY_SYNC_STATUS, user.getSyncStatus());
        values.put(KEY_SERVER_ID, user.getServerId());
        values.put(KEY_DELETED_STATUS, user.getDeletedStatus());

        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }


    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PIN, 0);
        values.put(KEY_SYNC_STATUS, user.getSyncStatus());
        values.put(KEY_DELETED_STATUS, user.getDeletedStatus());

        db.update(TABLE_USER, values, "id = ?", new String[]{String.valueOf(user.getId())});
        db.close(); // Closing database connection
    }

    public void updateUserSyncStatus(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PIN, 0);
        values.put(KEY_SYNC_STATUS, user.getSyncStatus());
        values.put(KEY_SERVER_ID, user.getServerId());

        db.update(TABLE_USER, values, "id = ?", new String[]{String.valueOf(user.getId())});

        db.close(); // Closing database connection
    }


    public void updateUserPin(int pin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PIN, pin);


        db.update(TABLE_USER, values, null, null);


        db.close(); // Closing database connection
    }

    public String getUserPhone() {
        String userPhone = "";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT phone FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                userPhone = cursor.getString(cursor.getColumnIndex(KEY_PHONE));

            } while (cursor.moveToNext());
        }

        database.close();


        return userPhone;
    }

    public String getUserName() {
        String userName = "";

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + KEY_NAME + " FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                userName = cursor.getString(cursor.getColumnIndex(KEY_NAME));

            } while (cursor.moveToNext());
        }

        database.close();


        return userName;
    }


    public List<User> getUndeletedUsers() {
        List<User> users = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "+KEY_DELETED_STATUS+" = ? ORDER BY id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(0)});

        if (cursor.moveToFirst()) {
            do {
                User user = new User(

                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_DELETED_STATUS))
                );

                users.add(user);

            } while (cursor.moveToNext());
        }

        database.close();

        return users;
    }


    public User getUser(int userId) {

        User user = null;

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE id = " + userId + " ORDER BY id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                user = new User(

                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_DELETED_STATUS))
                );

            } while (cursor.moveToNext());
        }

        database.close();

        return user;
    }

    public List<User> getUnsyncUsers(){
        List<User> users = new ArrayList<>();

        int SYNC_STATUS = 0;
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "+KEY_SYNC_STATUS+" = ?  ORDER BY id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(SYNC_STATUS)});

        if (cursor.moveToFirst()) {
            do {
                User user = new User(

                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_DELETED_STATUS))
                );

                users.add(user);

            } while (cursor.moveToNext());
        }

        database.close();

        return users;
    }

    public int deleteUser(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_USER, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }


    /*******************************************
     Begin street crude
     ********************************************/
    public void createAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, address.getName());
        values.put(KEY_CITY, address.getCity());
        values.put(KEY_COUNTRY, address.getCountry());
        values.put(KEY_SERVER_ID, address.getServerId());

        db.insert(TABLE_ADDRESS, null, values);


        db.close(); // Closing database connection
    }


    /*******************************************
     Begin business crude
     ********************************************/
    public void createBusiness(Business business) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, business.getName());
        values.put(KEY_ADDRESS_ID, business.getAddressId());
        db.insert(TABLE_BUSINESS, null, values);

        db.close(); // Closing database connection
    }

    public void updateBusiness(Business business) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, business.getName());
        values.put(KEY_ADDRESS_ID, business.getAddressId());
        db.update(TABLE_BUSINESS, values, "id = ?", new String[]{String.valueOf(business.getId())});
        db.close(); // Closing database connection
    }


    public List<Business> getBusinesses() {
        List<Business> businesses = new ArrayList<>();

        String selectQuery = "SELECT  businesses.id, businesses.name, businesses.server_id, addresses.name as address, addresses.id  as address_id , addresses.city , addresses.country, addresses.server_id "
                +" FROM " + TABLE_BUSINESS + " JOIN " + TABLE_ADDRESS + " ON addresses.id = businesses.address_id  ORDER BY businesses.id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Address address = new Address(
                        cursor.getInt(cursor.getColumnIndex(KEY_ADDRESS_ID)),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("city")),
                        cursor.getString(cursor.getColumnIndex("country")),
                        0
                );

                Business business = new Business(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_ADDRESS_ID)),
                        cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID))
                );

                business.setAddress(address);
                businesses.add(business);

            } while (cursor.moveToNext());
        }

        database.close();

        return businesses;
    }

    public Business getSelectedBusiness(int storeId) {

        Business business = null;

        String selectQuery = "SELECT  businesses.id, businesses.name,  addresses.name as address, businesses.server_id, addresses.id  as address_id , addresses.city , addresses.country, addresses.server_id "
                +" FROM " + TABLE_BUSINESS + " JOIN " + TABLE_ADDRESS + " ON addresses.id = businesses.address_id  WHERE businesses.id = ? ORDER BY businesses.id DESC ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,new  String[]{String.valueOf(storeId)});

        if (cursor.moveToFirst()) {

            Address address = new Address(
                    cursor.getInt(cursor.getColumnIndex(KEY_ADDRESS_ID)),
                    cursor.getString(cursor.getColumnIndex("address")),
                    cursor.getString(cursor.getColumnIndex("city")),
                    cursor.getString(cursor.getColumnIndex("country")),
                    0
            );

            business = new Business(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getInt(cursor.getColumnIndex(KEY_ADDRESS_ID)),
                    cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID))
                    );

            business.setAddress(address);
        }

        database.close();

        return business;

    }

    public int deleteBusiness(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_BUSINESS, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    /*******************************************
     Begin category crude
     ********************************************/
    public void createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        db.insert(TABLE_CATEGORY, null, values);


        db.close(); // Closing database connection
    }

    public void updateCategory(Category category) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());

        values.put(KEY_NAME, category.getName());

        db.update(TABLE_CATEGORY, values, "id = ?", new String[]{String.valueOf(category.getId())});


        db.close(); // Closing database connection
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        "",
                        0
                );


                categories.add(category);

            } while (cursor.moveToNext());
        }

        database.close();

        return categories;
    }

    public Category getCategory() {
        Category category = null;

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            category = new Category(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    "",
                    0
            );

        }

        database.close();

        return category;
    }

    public int deleteCategory(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_CATEGORY, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    /*******************************************
     Begin product crude
     ********************************************/
    public void createProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_COST, product.getCost());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_STOCK, product.getStock());
        values.put(KEY_DISCOUNT, product.getDiscount());
        values.put(KEY_IMAGE, product.getImgUrl());
        values.put(KEY_BARCODE, product.getBarcode());
        values.put(KEY_CATEGORY_ID, product.getCategoryId());

        db.insert(TABLE_PRODUCT, null, values);


        db.close(); // Closing database connection
    }

    public void updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_COST, product.getCost());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_STOCK, product.getStock());
        values.put(KEY_DISCOUNT, product.getDiscount());
        values.put(KEY_IMAGE, product.getImgUrl());
        values.put(KEY_BARCODE, product.getBarcode());
        values.put(KEY_CATEGORY_ID, product.getCategoryId());

        db.update(TABLE_PRODUCT, values, "id = ?", new String[]{String.valueOf(product.getId())});


        db.close(); // Closing database connection
    }

    public void updateProductStock(int stock, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STOCK, stock);

        db.update(TABLE_PRODUCT, values, "id = ?", new String[]{String.valueOf(productId)});


        db.close(); // Closing database connection
    }

    public void updateProductCategoryId(int productId, int categoryId) {

        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        ;
        values.put(KEY_CATEGORY_ID, categoryId);

        db.update(TABLE_PRODUCT, values, "id = ?", new String[]{String.valueOf(productId)});

        db.close(); // Closing database connection
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_PRICE))), cursor.getString(cursor.getColumnIndex(KEY_COST)),
                        cursor.getString(cursor.getColumnIndex(KEY_BARCODE)), cursor.getInt(cursor.getColumnIndex(KEY_DISCOUNT)),
                        cursor.getInt(cursor.getColumnIndex(KEY_STOCK)), cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), "", ""
                );

                products.add(product);

            } while (cursor.moveToNext());
        }

        database.close();

        return products;
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE category_id  = " + categoryId + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_PRICE))), cursor.getString(cursor.getColumnIndex(KEY_COST)),
                        cursor.getString(cursor.getColumnIndex(KEY_BARCODE)), cursor.getInt(cursor.getColumnIndex(KEY_DISCOUNT)),
                        cursor.getInt(cursor.getColumnIndex(KEY_STOCK)), cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), "", ""
                );

                products.add(product);

            } while (cursor.moveToNext());
        }

        database.close();

        return products;
    }

    public Product getProductById(int id) {
        Product product = null;

        String selectQuery = "SELECT " + "products.*, categories.name as category, skus.name as sku FROM " + TABLE_PRODUCT
                + " LEFT JOIN " + TABLE_CATEGORY + " ON categories.id = products.category_id "
                + " LEFT JOIN " + TABLE_PRODUCT_SKU + " ON products.id = product_skus.product_id "
                + " LEFT JOIN " + TABLE_SKU + " ON skus.id = product_skus.sku_id WHERE products.id = " + id;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_PRICE))), cursor.getString(cursor.getColumnIndex(KEY_COST)),
                    cursor.getString(cursor.getColumnIndex(KEY_BARCODE)), cursor.getInt(cursor.getColumnIndex(KEY_DISCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(KEY_STOCK)), cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), cursor.getString(cursor.getColumnIndex("category")),
                    cursor.getString(cursor.getColumnIndex("sku")));
        }

        database.close();

        return product;
    }

    public int deleteProduct(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_PRODUCT, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    /*******************************************
     Begin sku crude
     ********************************************/
    public void createSku(Sku sku) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, sku.getName());
        db.insert(TABLE_SKU, null, values);

        db.close(); // Closing database connection
    }

    public void attachSkuToProduct(int skuId, int productId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SKU_ID, skuId);
        values.put(KEY_PRODUCT_ID, productId);

        db.insert(TABLE_PRODUCT_SKU, null, values);
        db.close();
    }

    public void reAttachSkuToProduct(int skuId, int oldSkuId, int productId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("SKU_TEST", "" + skuId + " " + productId);

        ContentValues values = new ContentValues();
        values.put(KEY_SKU_ID, skuId);
        values.put(KEY_PRODUCT_ID, productId);

        String[] args = new String[]{"" + oldSkuId, "" + productId};
        db.update(TABLE_PRODUCT_SKU, values, "sku_id=? AND product_id=?", args);

        db.close();
    }

    public int getSkuIdByName(String name) {

        int id = 0;

        String selectQuery = "SELECT  id FROM " + TABLE_SKU + " WHERE name = '" + name + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
        }

        database.close();

        return id;
    }


    /*******************************************
     Begin expenses category crude
     ********************************************/
    public void createExpensesCategory(ExpensesCategory category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        values.put(KEY_AMOUNT, category.getAmount());

        db.insert(TABLE_EXPENSES_CATEGORY, null, values);

        db.close(); // Closing database connection
    }

    public void updateExpenseCategory(ExpensesCategory expensesCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, expensesCategory.getName());

        db.update(TABLE_EXPENSES_CATEGORY, values, "id = ?", new String[]{String.valueOf(expensesCategory.getId())});

        db.close(); // Closing database connection
    }


    public void updateExpensesCategoryAmnt(String amount, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_AMOUNT, amount);

        db.update(TABLE_EXPENSES_CATEGORY, values, "id = ?", new String[]{String.valueOf(id)});

        db.close(); // Closing database connection
    }


    public List<ExpensesCategory> getExpensesCategories() {
        List<ExpensesCategory> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES_CATEGORY + " ORDER BY expenses_categories.id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ExpensesCategory category = new ExpensesCategory(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("amount"))

                );

                categories.add(category);

            } while (cursor.moveToNext());
        }

        database.close();

        return categories;
    }


    public String getExpCategoryTotalAmt() {
        String totalAmnt = "";

        String selectQuery = "SELECT  SUM(amount) as amount FROM " + TABLE_EXPENSES_CATEGORY + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                totalAmnt = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT));


            } while (cursor.moveToNext());
        }

        database.close();

        return totalAmnt;
    }


    public int deleteExpenseCategory(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_EXPENSES_CATEGORY, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    /*******************************************
     Begin expense items crude
     ********************************************/
    public void createExpenseItem(ExpensesItem item, int expCategoryId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_AMOUNT, item.getAmount());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_EXPENSES_CATEGORY_ID, expCategoryId);

        db.insert(TABLE_EXPENSES_ITEM, null, values);

        db.close(); // Closing database connection
    }

    public void updateExpenseItem(ExpensesItem expensesItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, expensesItem.getName());
        values.put(KEY_AMOUNT, expensesItem.getAmount());


        db.update(TABLE_EXPENSES_ITEM, values, "id = ?", new String[]{String.valueOf(expensesItem.getId())});

        db.close(); // Closing database connection
    }

    public List<ExpensesItem> getExpItemsByCategory(int id) {
        List<ExpensesItem> items = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES_ITEM + " WHERE " + KEY_EXPENSES_CATEGORY_ID + " = " + id + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ExpensesItem item = new ExpensesItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE))
                );

                items.add(item);


            } while (cursor.moveToNext());
        }

        database.close();

        return items;
    }

    public ExpensesItem getExpItems(int id) {

        ExpensesItem item = null;

        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES_ITEM + " WHERE " + KEY_ID + " = " + id + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                item = new ExpensesItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(KEY_DATE))
                );


            } while (cursor.moveToNext());
        }

        database.close();

        return item;
    }

    public String getExpItemsTotalAmt(int id) {
        String totalAmnt = "";

        String selectQuery = "SELECT  SUM(amount) as amount FROM " + TABLE_EXPENSES_ITEM + " WHERE " + KEY_EXPENSES_CATEGORY_ID + " = " + id + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                totalAmnt = cursor.getString(cursor.getColumnIndex(KEY_AMOUNT));


            } while (cursor.moveToNext());
        }

        database.close();

        return totalAmnt;
    }

    public int deleteExpense(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_EXPENSES_ITEM, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    public int deleteExpenseItemByCategory(int categoryId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_EXPENSES_ITEM, "expenses_categories_id=?", new String[]{String.valueOf(categoryId)});

        return 1;
    }

    /*******************************************
     Begin order crude
     ********************************************/
    public void createOrder(Order order, int businessId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DEVICE_TIME, order.getDeviceTime());
        values.put(KEY_ORDER_NO, order.getOrderNo());
        values.put(KEY_BUSINESS_ID, businessId);
        values.put(KEY_STATUS, order.getStatus() );

        db.insert(TABLE_ORDER, null, values);
        db.close(); // Closing database connection
    }

    public void updateOrder(int status) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STATUS, status );

        db.update(TABLE_ORDER, values, "status = ?", new String[]{String.valueOf(0)});
        db.close(); // Closing database connection
    }


    public int getCurrentOrderId() {
        int id = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_ORDER + " WHERE " + KEY_STATUS + " =?", new String[]{String.valueOf(0)});

        if (cursor.moveToFirst()) {
            do {

                id = cursor.getInt(cursor.getColumnIndex(KEY_ID));


            } while (cursor.moveToNext());
        }


        db.close();

        return id;
    }

    /*******************************************
     Begin cart crude
     ********************************************/
    public void createCart(Cart cart, int orderId) {

        //add data to cart the first time addToCart button is clicked

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUANTITY, cart.getTotalQnty());
        values.put(KEY_TOTAL_AMOUNT, cart.getTotalAmount());
        values.put(KEY_PRODUCT_ID, cart.getProductId());
        values.put(KEY_ORDER_ID, orderId);

        db.insert(TABLE_CART, null, values);
        // db.close(); // Closing database connection
    }

    public void updateCart(Cart cart, int orderId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUANTITY, cart.getTotalQnty());
        values.put(KEY_TOTAL_AMOUNT, cart.getTotalAmount());

        db.update(TABLE_CART, values, "product_id = ? AND order_id = ?", new String[]{String.valueOf(cart.getProductId()),String.valueOf(orderId)});
        //  db.close();

    }

    public List<Cart> getCart() {

        List<Cart> carts = new ArrayList();

        String selectQuery = "SELECT  carts.id, carts.total_amount, carts.quantity, products.id as product_id,products.name ,products.price FROM " + TABLE_CART + " JOIN " + TABLE_PRODUCT + " ON products.id = carts.product_id JOIN "+TABLE_ORDER+" ON orders.id = carts.order_id WHERE orders.status = 0 ORDER BY carts." + KEY_ID + " DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_AMOUNT))),
                        cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_PRICE)))

                );

                carts.add(cart);


            } while (cursor.moveToNext());
        }

        database.close();

        return carts;
    }

    public boolean isCartEmpty() {

        boolean isEmpty;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT * FROM " + TABLE_CART + " JOIN "+TABLE_ORDER +" ON "+TABLE_ORDER+".id = "+TABLE_CART+".order_id WHERE "+TABLE_ORDER+" .status = 0";
        Cursor cursor = db.rawQuery(count, null);

        if (cursor.getCount() > 0) {
            isEmpty = false;
        } else {

            isEmpty = true;
        }

        /* Toast.makeText(c, ""+isEmpty, Toast.LENGTH_SHORT).show();*/

        return isEmpty;
    }

    public boolean isProductIdExist(int productId) {

        boolean exist;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT carts.product_id FROM " + TABLE_CART + " JOIN " + TABLE_ORDER + " ON orders.id = carts.order_id  WHERE carts." + KEY_PRODUCT_ID + " =? AND orders."+ KEY_STATUS + " =? ", new String[]{String.valueOf(productId),String.valueOf(0)});

        if (c.getCount() > 0) {
          //  Log.d("LOGHERE", "exist");
            exist = true;
        } else {
          //  Log.d("LOGHERE", "doesnt exist");
            exist = false;
        }
        return exist;

    }


    public int getCartAmount(int productId) {
        int amount = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT total_amount FROM " + TABLE_CART + " WHERE " + KEY_PRODUCT_ID + " =?", new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            do {

                amount = cursor.getInt(cursor.getColumnIndex(KEY_TOTAL_AMOUNT));


            } while (cursor.moveToNext());
        }


        db.close();

        return amount;
    }

    public int getCartQnty(int productId) {
        int qnty = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT quantity FROM " + TABLE_CART + " WHERE " + KEY_PRODUCT_ID + " =?", new String[]{String.valueOf(productId)});

        if (cursor.moveToFirst()) {
            do {

                qnty = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));


            } while (cursor.moveToNext());
        }


        db.close();

        return qnty;
    }

    public Double getCartTotalAmt() {
        double totalAmnt = 0;

        String selectQuery = "SELECT  SUM(total_amount) as amount FROM " + TABLE_CART + " JOIN " + TABLE_ORDER + " ON orders.id = carts.order_id WHERE orders.status = 0";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                try{
                    totalAmnt = Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
                }catch (NullPointerException e){

                }



            } while (cursor.moveToNext());
        }

        database.close();

        return totalAmnt;
    }

    public int getCartTotalQnty() {
        int qnty = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  SUM(quantity) as quantity FROM " + TABLE_CART+ " JOIN " + TABLE_ORDER + " ON orders.id = carts.order_id WHERE orders.status = 0", null);

        if (cursor.moveToFirst()) {
            do {

                qnty = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY));


            } while (cursor.moveToNext());
        }


        db.close();

        return qnty;
    }

    public int deleteCart() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_CART, null, null);

        return 1;
    }

    public int deleteCartItem(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_CART, "id=?", new String[]{String.valueOf(id)});

        return 1;
    }

    /*******************************************
     view database on the app
     ********************************************/

    //this method is requred for viewing database on app
    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"message"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }
}
