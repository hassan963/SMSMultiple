package com.hassanmashraful.myapplication.activity;

/**
 * Created by Hassan M.Ashraful on 4/12/2017.
 */

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.hassanmashraful.myapplication.R;
import com.hassanmashraful.myapplication.content.UserInfo;
import com.hassanmashraful.myapplication.dbhelper.SQLiteHelper;
import com.hassanmashraful.myapplication.dbhelper.SessionManager;
import com.hassanmashraful.myapplication.keys.Key_Value;
import com.hassanmashraful.myapplication.network.ConnectivityReceiver;
import com.hassanmashraful.myapplication.network.MyApplication;
import com.hassanmashraful.myapplication.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private TextView sentSMS, pendingSMS, incomingSMS, countSent, countPending;

    boolean isConnect;
    private Menu menu;
    private SessionManager sessionManager;

    //For server
    private RequestQueue requestQueue;

    private ArrayList<UserInfo> userInfos = new ArrayList<>();
    Timer timerOne, timerTWO;
    TimerTask taskOne, taskTWO;
    private static MainMenu sInstance;

    private VolleySingleton volleySingleton;
    private SQLiteHelper db;

    private static int deliverCount = 0, queueCount = 0;
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);


        sentSMS = (TextView) findViewById(R.id.sentSMS);
        pendingSMS = (TextView) findViewById(R.id.pendingSMS);
        incomingSMS = (TextView) findViewById(R.id.incomingSMS);
        countPending = (TextView) findViewById(R.id.countPending);
        countSent = (TextView) findViewById(R.id.countSent);

        sessionManager = new SessionManager(getApplicationContext());
        db = new SQLiteHelper(this);

        //sessionManager.checkLogin();


        sInstance = this;

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

        /*countShow = (TextView) findViewById(R.id.countShow);
        queueShow = (TextView) findViewById(R.id.queueShow);*/


        //Timer to getting data from aerver by calling callingHOST();
        final Handler handleTWO = new Handler();
         timerTWO = new Timer();
         taskTWO = new TimerTask() {
            @Override
            public void run() {
                handleTWO.post(new Runnable() {
                    public void run() {

                        Log.v("TIMER TWO****", " CALLING HOST ");
                        callingHOST();  //getting data from server and inserting into arraylist
                    }
                });
            }
        };
        timerTWO.schedule(taskTWO, 0, 120000);

        //Timer to sending sms and removing data from arraylist and db by calling sendSMS()
        final Handler handler = new Handler();
         timerOne = new Timer();
         taskOne = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        Log.v("TIMER****", " "+userInfos.size());
                        for (int i = 0; i<userInfos.size(); i++) {
                            sendSMS( userInfos.get(i).getId(), userInfos.get(i).getNumber(), userInfos.get(i).getSms(), userInfos.get(i).getStatus()); //sending sms to number

                        }
                    }
                });
            }
        };
        timerOne.schedule(taskOne, 0, 4000); //it executes this every 4000ms



        sentSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "SEND", Toast.LENGTH_SHORT).show();

                // Launching the login activity
                Intent intent = new Intent(MainMenu.this, SMSDetails.class);
                intent.putExtra("smslayout", "Delivered");
                startActivity(intent);
                //finish();
            }
        });

        pendingSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "pending", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainMenu.this, SMSDetails.class);
                intent.putExtra("smslayout", "Pending");
                startActivity(intent);
            }
        });

        incomingSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainMenu.this, "incoming", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, SMSDetails.class);
                intent.putExtra("smslayout", "Inbox");
                startActivity(intent);
            }
        });


        isConnect = ConnectivityReceiver.isConnected();;


    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);

        changeIcon(isConnect);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.admin) {
            Intent intent = new Intent(MainMenu.this, Admin_Activity.class);
            startActivity(intent);
            //finish();
        }
        if (id == R.id.netWork){
            isConnect = ConnectivityReceiver.isConnected();
            changeIcon(isConnect);
        }
        if (id == R.id.logout){
            if (taskOne != null) taskOne.cancel();
            if (taskTWO != null) taskTWO.cancel();
            sessionManager.logoutUser();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    public void changeIcon(boolean isConnected){

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem item = menu.findItem(R.id.netWork);
                    if (item != null) {
                        item.setIcon(R.drawable.wii);
                    }
                }
            }
        });*/

        if (menu != null) {
            MenuItem item = menu.findItem(R.id.netWork);
            if (item != null) {
                if (isConnected)item.setIcon(R.drawable.wi);
                else item.setIcon(R.drawable.wii);
            }
        }


    }

    /*private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }*/


    //getting data from server
    public void callingHOST(){

        db.addPendingSMS("1","2","1");
        volleySingleton = volleySingleton.getInstance();  requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Key_Value.URL_GET_DATA, (String)null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJsonResponse(response); //parsing json data
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getAppContext(), "Error connection", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }


    //parsing json data
    private void parseJsonResponse(JSONObject response){

        if (response == null || response.length() == 0){return;}

        try {

            if (response.has(Key_Value.KEY_CONTACTS)){
                JSONArray jsonArray = response.getJSONArray(Key_Value.KEY_CONTACTS);

                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject currentData = jsonArray.getJSONObject(i);
                    //parsing data by individual key
                    String id = currentData.getString(Key_Value.KEY_ID);
                    String number = currentData.getString(Key_Value.KEY_NUMBER);
                    String sms = currentData.getString(Key_Value.KEY_SMS);
                    String status = currentData.getString(Key_Value.KEY_STATUS);

                    userInfos.add(new UserInfo(id, number, sms, status)); //adding data to arraylist
                    db.addPendingSMS(id, number, sms);
                    queueCount++;

                }
                for (int m = 0; m<jsonArray.length(); m++)
                    Toast.makeText(getAppContext(), "Num: "+userInfos.get(m).getNumber()+" ID: "+userInfos.get(m).getId()+" SMS: "+userInfos.get(m).getSms()+" STATUS: "+userInfos.get(m).getStatus(), Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        countPending.setText(String.valueOf(queueCount));

    }


    //sending sms to individual number
    private void sendSMS(final String id, final String number, final String message, final String status) {
        String SENT = "sent";
        String DELIVERED = "delivered";

        Intent sentIntent = new Intent(SENT);

        /*Create Pending Intents*/
        PendingIntent sentPI = PendingIntent.getBroadcast(
                getApplicationContext(), 0, sentIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent deliveryIntent = new Intent(DELIVERED);

        PendingIntent deliverPI = PendingIntent.getBroadcast(
                getApplicationContext(), 0, deliveryIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
     /* Register for SMS send action */
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String result = "";

                //checking the transmission is successful or not
                switch (getResultCode()) {

                    case AppCompatActivity.RESULT_OK:
                        result = "1";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        result = "0";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        result = "0";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        result = "0";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        result = "0";
                        break;
                }
                if (result.equals("1")){
                    Log.v("SAVING DATA ********  ", id+" %%%%");

                    save(id, number, message, status);
                }

            }

        }, new IntentFilter(SENT));
     /* Register for Delivery event */
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


            }

        }, new IntentFilter(DELIVERED));


        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, sentPI, deliverPI);


        //iterating the arraylist to remove the confirmed send sms object
        Iterator<UserInfo> iter = userInfos.iterator();
        while (iter.hasNext())
        {
            UserInfo user = iter.next();
            if(user.getId().equals(id))
            {
                //Use iterator to remove this User object.
                iter.remove();
                ++deliverCount;
                countSent.setText(String.valueOf(deliverCount));
                Log.v("REMOVED  ", id+" %%%%");
            }
        }

        Log.v("SMS SEND ", number+" %%%%");

    }

    //sending response to server after sending sms to numbet
    public void save(final String id, final String number, final String sms, final String status) {

        volleySingleton = volleySingleton.getInstance();  requestQueue = volleySingleton.getRequestQueue();

        //insertion
        StringRequest strReq = new StringRequest(Request.Method.POST, Key_Value.URL_SEND_RESPOSE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id", id);
                params.put("number", number);
                params.put("sms", sms);
                params.put("status", status);

                return params;
            }

        };

        // Adding request to request queue
        requestQueue.add(strReq);
        Log.v("SMS RESPONSE  ", number+" %%%%");
        //deliverCount++;
        if (queueCount>0)queueCount--;
        countPending.setText(String.valueOf(queueCount));


    }



    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        changeIcon(isConnected);
    }
}
