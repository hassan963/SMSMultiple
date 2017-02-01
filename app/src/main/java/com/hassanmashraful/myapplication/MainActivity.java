package com.hassanmashraful.myapplication;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hassanmashraful.myapplication.content.UserInfo;
import com.hassanmashraful.myapplication.dbhelper.SQLiteHelper;
import com.hassanmashraful.myapplication.keys.Key_Value;
import com.hassanmashraful.myapplication.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;


public class MainActivity extends AppCompatActivity {

    Button auto, responseBTN, csvBTN;

    //MySingleton mySingleton;
    RequestQueue requestQueue;
    SQLiteHelper dataBaseHelper;
    SQLiteDatabase sqLiteDatabase;

    // progress dialog initialization
    private ProgressDialog _progressDialog;
    private int _progress = 0;
    private Handler _progressHandler;
    ArrayList<String> numer = new ArrayList<>();

    ArrayList<UserInfo> userInfos = new ArrayList<>();

    private static MainActivity sInstance;

    TextView txt;

    VolleySingleton volleySingleton;

    private ProgressBar progressBar;
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sInstance = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txt = (TextView) findViewById(R.id.output);
        progressBar.setMax(10);

        auto = (Button) findViewById(R.id.auto);
        responseBTN = (Button) findViewById(R.id.responseBTN);
        csvBTN = (Button) findViewById(R.id.csvBTN);
        //numer.add("+8801521207588");
        numer.add("+8801924077150"); numer.add("+8801787695911");

        //mySingleton = mySingleton.getInstance(getApplicationContext());
        //requestQueue = mySingleton.getRequestQueue();


        csvBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportCSV();
            }
        });



        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*

                //Integer amount = Integer.parseInt(am);

                // display progress dialog
                showDialog(1);

                _progress = 0;
                _progressDialog.setProgress(0);
                _progressHandler.sendEmptyMessage(0);

                // send a block message of char Z

                */
                //for (String v : numer)

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                new MyTask().execute(10);

                /*
                for (int i = 0; i<userInfos.size(); i++)
                    sendSMS(userInfos.get(i).getNumber(), userInfos.get(i).getSms()); */
                //for (int i = 0; i<20; i++)


            }
        });
        /*

        // start out progress handler thread
        _progressHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (_progress >= 100) {
                    _progressDialog.dismiss();
                } else {
                    _progress++;
                    _progressDialog.incrementProgressBy(1);
                    _progressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };

        */
        responseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                volleySingleton = volleySingleton.getInstance();  requestQueue = volleySingleton.getRequestQueue();

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://104.171.117.35/sms/sendsms", (String)null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getAppContext(), response.toString(), Toast.LENGTH_LONG).show();
                        parseJsonResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getAppContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);

            }
        });




    }

    private void parseJsonResponse(JSONObject response){
        if (response == null || response.length() == 0){return;}

        try {
            //StringBuilder stringBuilder = new StringBuilder();
            if (response.has(Key_Value.KEY_CONTACTS)){
                JSONArray jsonArray = response.getJSONArray(Key_Value.KEY_CONTACTS);
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject currentData = jsonArray.getJSONObject(i);
                    String id = currentData.getString(Key_Value.KEY_ID);
                    String number = currentData.getString(Key_Value.KEY_NUMBER);
                    String sms = currentData.getString(Key_Value.KEY_SMS);
                    String status = currentData.getString(Key_Value.KEY_STATUS);
                    //stringBuilder.append(id+"\n");
                    userInfos.add(new UserInfo(id, number, sms, status));

                }
                for (int i = 0; i<jsonArray.length(); i++)
                    Toast.makeText(getAppContext(), "Num: "+userInfos.get(i).getNumber()+" ID: "+userInfos.get(i).getId()+" SMS: "+userInfos.get(i).getSms()+" STATUS: "+userInfos.get(i).getStatus(), Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendSMS(final String id, final String phoneNumber, final String message) {
        String SENT = "sent";
        String DELIVERED = "delivered";


        dataBaseHelper = new SQLiteHelper(sInstance);
        sqLiteDatabase = dataBaseHelper.getWritableDatabase();

        //SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, null, null);

        //Toast.makeText(getAppContext(), phoneNumber, Toast.LENGTH_SHORT).show();
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

                //getApplicationContext().unregisterReceiver(this);
                switch (getResultCode()) {

                    case AppCompatActivity.RESULT_OK:
                        result = "Transmission successful";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        result = "Transmission failed";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        result = "No service";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        result = "No PDU defined";
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        result = "No service";
                        break;
                }


                Toast.makeText(getApplicationContext(), result,
                        Toast.LENGTH_SHORT).show();
                dataBaseHelper.addSMSReport(id, phoneNumber, message, result, sqLiteDatabase);
            }

        }, new IntentFilter(SENT));
     /* Register for Delivery event */
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //getApplicationContext().unregisterReceiver(this);
                Toast.makeText(getApplicationContext(), "XXXX",
                        Toast.LENGTH_SHORT).show();
            }

        }, new IntentFilter(DELIVERED));

      /*Send SMS*/
        //SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(phoneNo, null, msg, sentPI, deliverPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);


    }






    /*

    // build  our progress handler dialog message display
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                _progressDialog = new ProgressDialog(this);
                _progressDialog.setIcon(R.drawable.icon);
                _progressDialog.setTitle("Sending " + " message(s)...");
                _progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                _progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Hide", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton)
                            {
                                Toast.makeText(getBaseContext(),
                                        "Running in background...", Toast.LENGTH_SHORT).show();
                            }
                        });
                _progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton)
                            {
                                Toast.makeText(getBaseContext(),
                                        "Cancelled!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                return _progressDialog;
        }
        return null;
    }

    */

    class MyTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

            for (int i = 0; i<2; i++) {
                sendSMS(userInfos.get(i).getId(),userInfos.get(i).getNumber(), userInfos.get(i).getSms());
                try {
                    Thread.sleep(1000);

                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            dataBaseHelper.close();
            /*
            for (; count <= params[0]; count++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            */

            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            txt.setText(result);
            //btn.setText("Restart");
        }
        @Override
        protected void onPreExecute() {
            txt.setText("Task Starting...");
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            txt.setText("Running..."+ values[0]);
            progressBar.setProgress(values[0]);
        }
    }

    public void exportCSV() {
        dataBaseHelper = new SQLiteHelper(getApplicationContext());


        File dbFile = getDatabasePath(dataBaseHelper.getDBname());
        SQLiteHelper dbhelper = new SQLiteHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM sms_report", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }


    }




}
