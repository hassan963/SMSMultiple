package com.hassanmashraful.myapplication.keys;

/**
 * Created by Hassan M.Ashraful on 2/1/2017.
 */

public class Key_DB {

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "android_api";

    // Login table name
    public static final String TABLE_SMS_REPORT = "sms_report";

    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_SMS = "sms";
    public static final String KEY_STATUS = "status";

    public static final String SMS_INFO_CREATE_QUERY = "CREATE TABLE "+ TABLE_SMS_REPORT +"("+ KEY_ID+" TEXT,"+ KEY_NUMBER +" TEXT,"+ KEY_SMS +" TEXT,"+ KEY_STATUS +" TEXT);";



}
