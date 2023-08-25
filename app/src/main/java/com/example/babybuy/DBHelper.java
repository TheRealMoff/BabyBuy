package com.example.babybuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "babybuy.db";
    final String BABYITEMS = "BABYITEMS";
    final String TABLE_USERS = "USERS";
    
    private static final String ID = "ID";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";

    private static final String ITEM_ID = "ITEM_ID";
    private static final String ITEM_IMAGE = "ITEM_IMAGE";
    private static final String ITEM_DESCRIPTION = "ITEM_DESCRIPTION";
    private static final String ITEM_PRICE = "ITEM_PRICE";

    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;
    Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_USERS_TABLE = "CREATE TABLE USERS("+
                " " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + FULL_NAME + " STRING," +
                " " + EMAIL + " STRING," +
                " " + PASSWORD + " STRING)";
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);

        String CREATE_BABY_ITEMS_TABLE = "CREATE TABLE " + BABYITEMS + "(" +
                " " + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + ITEM_IMAGE + " BLOB," +
                " " + ITEM_DESCRIPTION + " TEXT," +
                " " + ITEM_PRICE + " FLOAT)";
        sqLiteDatabase.execSQL(CREATE_BABY_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sq, int i, int i1) {

        sq.execSQL("DROP TABLE IF EXISTS USERS");
        this.onCreate(sq);

        sq.execSQL("DROP TABLE IF EXISTS " + BABYITEMS);
        this.onCreate(sq);
    }

    public boolean registerUser(Users users){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(FULL_NAME, users.getFullName());
            cv.put(EMAIL, users.getEmail());
            cv.put(PASSWORD, users.getPassword());

            db.insert(TABLE_USERS, null, cv);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkEmailPass(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?",new String[]{email,password});

        if (cursor.getCount()>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean insertItem(BabyItems babyItems){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Bitmap selectedImage = babyItems.getItemImage();
            byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imageInBytes = byteArrayOutputStream.toByteArray();

            ContentValues cv = new ContentValues();
            cv.put(ITEM_IMAGE, imageInBytes);
            cv.put(ITEM_DESCRIPTION, babyItems.getDescription());
            cv.put(ITEM_PRICE, babyItems.getPrice());

            db.insert("BABYITEMS",null, cv);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<BabyItems> getAllItems(){
        List<BabyItems> babyItemsList = new LinkedList<>();
        String query = "SELECT * FROM " +BABYITEMS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToNext()){
            do {
                int id = cursor.getInt(0);
                byte[] imageByte = cursor.getBlob(1);
                String description = cursor.getString(2);
                Float price = cursor.getFloat(3);

                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                babyItemsList.add(new BabyItems(imageBitmap,description,price));

            } while (cursor.moveToNext());
        }
        db.close();
        return babyItemsList;
    }

    boolean updateData(BabyItems babyItems){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(ITEM_ID,babyItems.getId());
        cv.put(ITEM_IMAGE, String.valueOf(babyItems.getItemImage()));
        cv.put(ITEM_DESCRIPTION, babyItems.getDescription());
        cv.put(ITEM_PRICE, babyItems.getPrice());

        long result = db.update(BABYITEMS, cv,"ITEM_ID=?", new String[]{String.valueOf(babyItems.id)});

        if(result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();

        }
        return false;
    }

    void deleteOneRow(BabyItems babyItems){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(BABYITEMS, "ITEM_ID=?", new String[]{String.valueOf(babyItems.id)});
        if(result == -1){

            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
