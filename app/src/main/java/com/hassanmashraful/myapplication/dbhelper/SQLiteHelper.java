package com.hassanmashraful.myapplication.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hassanmashraful.myapplication.keys.Key_DB;


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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+Key_DB.SMS_INFO_CREATE_QUERY);
        onCreate(db);
    }


    public void addSMSReport(String id, String number, String sms, String status, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Key_DB.KEY_ID, id);
        contentValues.put(Key_DB.KEY_NUMBER, number);
        contentValues.put(Key_DB.KEY_SMS, sms);
        contentValues.put(Key_DB.KEY_STATUS, status);

        db.insert(Key_DB.TABLE_SMS_REPORT, null, contentValues);
        Log.e("Database Teacher", "One row inserted");
    }

    public String getDBname(){
        return Key_DB.DATABASE_NAME;
    }


}
