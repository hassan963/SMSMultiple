package com.hassanmashraful.myapplication.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hassanmashraful.myapplication.R;
import com.hassanmashraful.myapplication.dbhelper.SessionManager;

import java.util.HashMap;

/**
 * Created by Hassan M.Ashraful on 4/14/2017.
 */

public class Admin_Activity extends AppCompatActivity {

    private SessionManager sessionManager;

    private ImageView ad, editPass;
    private TextView about, resetPass, phnNum;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sessionManager = new SessionManager(getApplicationContext());
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ad = (ImageView) findViewById(R.id.ad);
        editPass = (ImageView) findViewById(R.id.editPass);

        about = (TextView) findViewById(R.id.about);
        resetPass = (TextView) findViewById(R.id.resetPass);
        phnNum = (TextView) findViewById(R.id.phnNum);

        HashMap<String, String> user =  sessionManager.getUserDetails();
        about.setText(user.get("email"));

        /*TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();*/

        /*String main_data[] = {"data1", "is_primary", "data3", "data2", "data1", "is_primary", "photo_uri", "mimetype"};
        Object object = getContentResolver().query(Uri.withAppendedPath(android.provider.ContactsContract.Profile.CONTENT_URI, "data"),
                main_data, "mimetype=?",
                new String[]{"vnd.android.cursor.item/phone_v2"},
                "is_primary DESC");
        if (object != null) {
            do {
                if (!((Cursor) (object)).moveToNext())
                    break;
                // This is the phoneNumber
                s1 = ((Cursor) (object)).getString(4);
            } while (true);
            ((Cursor) (object)).close();
        }*/


        editPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //new AlertDialog.Builder(getApplicationContext(), R.style.MyCustomDialogTheme);

                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);

                //if you want to change the theme of the dialog.

                final EditText edittext = new EditText(getApplicationContext());
                alert.setMessage("Enter your password");
                alert.setTitle("Reset Password");

                alert.setView(edittext);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        //Editable YouEditTextValue = edittext.getText();
                        //OR
                        String s = edittext.getText().toString();
                        sessionManager.changePassword(s);

                    }
                });


                alert.show();


            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Admin_Activity.this, MainMenu.class);
        startActivity(intent);
        Log.v("abck", "main menu");
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
