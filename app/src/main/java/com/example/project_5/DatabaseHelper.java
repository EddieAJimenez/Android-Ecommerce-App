package com.example.project_5;


import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.project_5.Item;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserStore.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla de usuarios
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_REAL_NAME = "real_name";
    public static final String COLUMN_USER_ADDRESS = "address";

    // Tabla de ventas
    private static final String TABLE_SALES = "sales";
    private static final String COLUMN_SALE_ID = "sale_id";
    private static final String COLUMN_SALE_ITEM_NAME = "item_name";
    private static final String COLUMN_SALE_PRICE = "price";
    private static final String COLUMN_SALE_USER_ID = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_REAL_NAME + " TEXT,"
                + COLUMN_USER_ADDRESS + " TEXT" + ")";

        String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "("
                + COLUMN_SALE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SALE_ITEM_NAME + " TEXT,"
                + COLUMN_SALE_PRICE + " REAL,"
                + COLUMN_SALE_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_SALE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        onCreate(db);
    }

    public void addUser(String userName, String password, String realName, String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_REAL_NAME, realName);
        values.put(COLUMN_USER_ADDRESS, address);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public List<Item> getUserSales(int userId) {
        List<Item> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SALES,
                new String[] {COLUMN_SALE_ID, COLUMN_SALE_ITEM_NAME, COLUMN_SALE_PRICE},
                COLUMN_SALE_USER_ID + "=?",
                new String[] {String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SALE_ITEM_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALE_PRICE));
                sales.add(new Item(itemName, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sales;
    }

    public int getUserId(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_ID};
        String selection = COLUMN_USER_NAME + "=? AND " + COLUMN_USER_PASSWORD + "=?";
        String[] selectionArgs = {userName, password};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }
        cursor.close();
        db.close();

        return userId;
    }
    public void addSale(Item item, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SALE_ITEM_NAME, item.getName());
        values.put(COLUMN_SALE_PRICE, item.getPrice());
        values.put(COLUMN_SALE_USER_ID, userId);
        Log.d("DatabaseHelper", "User ID in addSale: " + userId);

        db.insert(TABLE_SALES, null, values);
        db.close();
    }

    public boolean hasItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT count(*) FROM " + TABLE_SALES;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void addItem(Item item) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.query(TABLE_SALES,
            new String[] {COLUMN_SALE_ITEM_NAME},
            COLUMN_SALE_ITEM_NAME + "=?",
            new String[] {item.getName()},
            null, null, null);

    if (!cursor.moveToFirst()) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SALE_ITEM_NAME, item.getName());
        values.put(COLUMN_SALE_PRICE, item.getPrice());

        db.insert(TABLE_SALES, null, values);
    }

    cursor.close();
    db.close();
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SALES,
                new String[] {COLUMN_SALE_ITEM_NAME, COLUMN_SALE_PRICE},
                COLUMN_SALE_USER_ID + " IS NULL",
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SALE_ITEM_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALE_PRICE));
                items.add(new Item(name, price));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return items;
    }

    public void deleteAllSales() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SALES);
        db.close();
    }

}
