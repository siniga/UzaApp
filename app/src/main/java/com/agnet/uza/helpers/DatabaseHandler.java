package com.agnet.uza.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.agnet.uza.models.Category;
import com.agnet.uza.models.Product;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context c;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 15;

    // Database Name
    private static final String DATABASE_NAME = "uza";


    // table names
    private static final String TABLE_USER = "users";
    private static final String TABLE_STREET = "streets";
    private static final String TABLE_BUSINESS = "businesses";
    private static final String TABLE_USER_BUSINESS = "user_business";
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_UNIT = "units";
    private static final String TABLE_SKU = "skus";
    private static final String TABLE_PRODUCT_UNIT = "product_units";
    private static final String TABLE_PRODUCT_SKU = "product_skus";

    //user table
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PIN = "pin";
    private static final String KEY_USER_ID = "user_id";

    //streets table
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STREET_ID = "street_id";

    //business
    private static final String KEY_BUSINESS_ID = "business_id";

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
    private static final String KEY_UNIT_ID  = "unit_id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PIN + " INTEGER,"
                + KEY_PHONE + " TEXT " + ")";

        String CREATE_STREET_TABLE = "CREATE TABLE " + TABLE_STREET + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT " + ")";

        String CREATE_BUSINESS_TABLE = "CREATE TABLE " + TABLE_BUSINESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_STREET_ID + " INTEGER " + ")";

        String CREATE_USER_BUSINESS = "CREATE TABLE " + TABLE_USER_BUSINESS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_ID + " TEXT,"
                + KEY_BUSINESS_ID + " INTEGER " + ")";

        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY+ "("
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


        String CREATE_SKU_TABLE = "CREATE TABLE " + TABLE_SKU+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT" + ")";

        String CREATE_UNIT_TABLE = "CREATE TABLE " + TABLE_UNIT+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_CATEGORY_ID + " INTEGER" + ")";

        String CREATE_PRODUCT_SKU_TABLE = "CREATE TABLE " + TABLE_PRODUCT_SKU+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_SKU_ID + " INTEGER,"
                + KEY_PRODUCT_ID + " INTEGER" + ")";

        String CREATE_PRODUCT_UNIT_TABLE = "CREATE TABLE " + TABLE_PRODUCT_UNIT+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_UNIT_ID + " INTEGER,"
                + KEY_PRODUCT_ID + " INTEGER" + ")";



        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_STREET_TABLE);
        db.execSQL(CREATE_BUSINESS_TABLE);
        db.execSQL(CREATE_USER_BUSINESS);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_SKU_TABLE);
        db.execSQL(CREATE_UNIT_TABLE);
        db.execSQL(CREATE_PRODUCT_SKU_TABLE);
        db.execSQL(CREATE_PRODUCT_UNIT_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STREET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_BUSINESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SKU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_UNIT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SKU);



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

        boolean isEmpty = false;

        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT * FROM " + table;
        Cursor cursor = db.rawQuery(count, null);

        if (cursor.getCount() > 0) {
//            Toast.makeText(c, "" + isEmpty, Toast.LENGTH_SHORT).show();
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
    public void createOrUpdateUser(String phone, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PHONE, phone);
        values.put(KEY_NAME, name);

        //if user table is empty, create user
        //otherwise update user number to indicate that number has been changed
        if (isTableEmpty(TABLE_USER)) {
            db.insert(TABLE_USER, null, values);
        } else {
            db.update(TABLE_USER, values, null, null);
        }


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


    /*******************************************
     Begin street crude
     ********************************************/
    public void createStreet(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        db.insert(TABLE_STREET, null, values);


        db.close(); // Closing database connection
    }


    /*******************************************
     Begin business crude
     ********************************************/
    public void createBusiness(String name, int streetId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_STREET_ID, streetId);
        db.insert(TABLE_BUSINESS, null, values);


        db.close(); // Closing database connection
    }

    public void createUserBusiness(int userId, int businessId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_BUSINESS_ID, businessId);
        db.insert(TABLE_USER_BUSINESS, null, values);


        db.close(); // Closing database connection
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

    public List<Category> getCategories(){
        List<Category> categories = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY +" ORDER BY "+KEY_ID+" DESC";
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

    public void updateProductCategoryId(int productId, int categoryId) {

        SQLiteDatabase db = this.getWritableDatabase();

        Toast.makeText(c, ""+productId +" "+categoryId, Toast.LENGTH_SHORT).show();
        ContentValues values = new ContentValues();;
        values.put(KEY_CATEGORY_ID, categoryId);

        db.update(TABLE_PRODUCT, values, "id = ?", new String[]{String.valueOf(productId)});

        db.close(); // Closing database connection
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT +" ORDER BY "+KEY_ID+" DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getString(cursor.getColumnIndex(KEY_PRICE)), cursor.getString(cursor.getColumnIndex(KEY_COST)),
                        cursor.getString(cursor.getColumnIndex(KEY_BARCODE)), cursor.getInt(cursor.getColumnIndex(KEY_DISCOUNT)),
                        cursor.getInt(cursor.getColumnIndex(KEY_STOCK)), cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)),""
                );

                products.add(product);

            } while (cursor.moveToNext());
        }

        database.close();

        return products;
    }

    public Product getProductById(int id) {
        Product product =  null;

        String selectQuery = "SELECT products.*, categories.name as category FROM " + TABLE_PRODUCT +" INNER JOIN "+TABLE_CATEGORY+" " +
                " ON categories.id = products.category_id WHERE products.id = "+id;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            product = new Product(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_PRICE)), cursor.getString(cursor.getColumnIndex(KEY_COST)),
                    cursor.getString(cursor.getColumnIndex(KEY_BARCODE)), cursor.getInt(cursor.getColumnIndex(KEY_DISCOUNT)),
                    cursor.getInt(cursor.getColumnIndex(KEY_STOCK)), cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)), cursor.getString(cursor.getColumnIndex("category")));
        }

        database.close();

        return product;
    }
    public int deleteProduct(int id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_PRODUCT, "id=?", new String[] { String.valueOf(id)} );

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
