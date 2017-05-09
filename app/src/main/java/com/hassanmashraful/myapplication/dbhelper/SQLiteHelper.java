package com.hassanmashraful.myapplication.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hassanmashraful.myapplication.content.UserInfo;
import com.hassanmashraful.myapplication.keys.Key_DB;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Hassan M.Ashraful on 2/1/2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHelper.class.getSimpleName();

    public SQLiteHelper(Context context) {
        super(context, Key_DB.DATABASE_NAME, null, Key_DB.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Key_DB.SMS_INFO_CREATE_QUERY);
        db.execSQL(Key_DB.SMS_PENDING_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Key_DB.SMS_PENDING_CREATE_QUERY);
        db.execSQL("DROP TABLE IF EXISTS "+Key_DB.SMS_INFO_CREATE_QUERY);
        onCreate(db);
    }


    public void addPendingSMS(String id, String number, String sms){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_DB.KEY_ID, id);
        contentValues.put(Key_DB.KEY_NUMBER, number);
        contentValues.put(Key_DB.KEY_SMS, sms);

        db.insert(Key_DB.TABLE_SMS_PENDING, null, contentValues);

        db.close(); // Closing database connection
        Log.e("Database Teacher", "One row inserted");
    }

    // Getting All Contacts
    public List<UserInfo> getAllContacts() {
        List<UserInfo> contactList = new ArrayList<UserInfo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Key_DB.TABLE_SMS_PENDING;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserInfo contact = new UserInfo();
                contact.setId(cursor.getString(0));
                contact.setNumber(cursor.getString(1));
                contact.setSms(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + Key_DB.TABLE_SMS_PENDING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Deleting single contact
    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        /*db.delete(Key_DB.TABLE_SMS_REPORT, Key_DB.KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });*/
        db.delete(Key_DB.TABLE_SMS_PENDING, Key_DB.KEY_ID + " = ?",
                new String[] { id });
        db.close();
    }


    public String getDBname(){
        return Key_DB.DATABASE_NAME;
    }


}
